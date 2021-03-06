package com.example.comuse_kotlin.fireStoreService

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.comuse_kotlin.dataModel.Member
import com.example.comuse_kotlin.repository.MembersRepository

import com.google.firebase.firestore.DocumentChange
import io.reactivex.Observable
import io.reactivex.subjects.ReplaySubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MembersServiceManager(private val application: Application) {
    private var membersList: ArrayList<Member> = ArrayList()

    public var membersSubject: ReplaySubject<ArrayList<Member>> = ReplaySubject.createWithSize(100)

    private val membersRepository: MembersRepository by lazy {
        MembersRepository(application)
    }
    fun getMembersFromFireStore() {
        FirebaseVar.user?.let { _ ->
            FirebaseVar.dbFIB?.let { db ->
                FirebaseVar.memberListener = db.collection("Members").addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.d(TAG, "SnapShot Listen Error", e)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        for (dc in snapshot.documentChanges) {
                            val member: Member = dc.document.toObject(Member::class.java)
                            when (dc.type) {
                                DocumentChange.Type.ADDED -> {
                                    if (member.inoutStatus) { membersList.add(0,member) }
                                    else { membersList.add(member) }

                                    //notify repository
                                    membersRepository.addMemberToLocal(member)
                                }
                                DocumentChange.Type.MODIFIED -> {
                                    for (compare: Member in membersList) {
                                        if (compare.email == member.email) {
                                            var index: Int = membersList.indexOf(compare)
                                            membersList.removeAt(index)
                                            if (member.inoutStatus) { membersList.add(0,member) }
                                            else { membersList.add(member) }

                                            //notify repository
                                            membersRepository.updateMemberToLocal(member)
                                            break;
                                        }
                                    }
                                }
                                DocumentChange.Type.REMOVED -> {
                                    for (compare: Member in membersList) {
                                        if (compare.email.equals(member.email)) {
                                            val index: Int = membersList.indexOf(compare)
                                            membersList.removeAt(index)

                                            //notify repository
                                            membersRepository.removeMemberToLocal(member)
                                            break;

                                        }
                                    }
                                }
                            }
                        }

                        membersSubject.onNext(membersList)
                    }
                }
            }
        }
    }
    fun clearList() {
        this.membersList.clear()
    }
}
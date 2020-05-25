package com.example.comuse_kotlin.fireStoreService

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.comuse_kotlin.dataModel.Member
import com.example.comuse_kotlin.repository.MembersRepository

import com.google.firebase.firestore.DocumentChange

class MembersServiceManager(private val application: Application) {
    private var membersList: ArrayList<Member> = ArrayList()
    private val membersRepository: MembersRepository by lazy {
        MembersRepository(application)
    }
    fun getMembersFromFireStore(members: MutableLiveData<ArrayList<Member>>) {
        FirebaseVar.user?.let { _ ->
            FirebaseVar.dbFIB?.let { db ->
                db?.collection("Members")?.addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.d(TAG, "SnapShot Listen Error", e)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        for (dc in snapshot.documentChanges) {
                            val member: Member = dc.document.toObject(Member::class.java)
                            when (dc.type) {
                                DocumentChange.Type.ADDED -> {
                                    membersList.add(member)

                                    //notify repository
                                    membersRepository.addMember(member)
                                }
                                DocumentChange.Type.MODIFIED -> {
                                    for (compare: Member in membersList) {
                                        if (compare.email == member.email) {
                                            val index: Int = membersList.indexOf(compare)
                                            membersList.removeAt(index)
                                            membersList.add(index, member)

                                            //notify repository
                                            membersRepository.updateMember(member)
                                        }
                                    }
                                }
                                DocumentChange.Type.REMOVED -> {
                                    for (compare: Member in membersList) {
                                        if (compare.email.equals(member.email)) {
                                            val index: Int = membersList.indexOf(compare)
                                            membersList.removeAt(index)

                                            //notify repository
                                            membersRepository.removeMember(member)
                                        }
                                    }
                                }
                            }
                        }
                        members.postValue(membersList)
                    }
                }
            }
        }
    }
}
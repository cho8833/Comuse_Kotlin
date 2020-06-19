package com.example.comuse_kotlin.fireStoreService


import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.comuse_kotlin.dataModel.Member
import com.example.comuse_kotlin.repository.UserDataRepository
import io.reactivex.subjects.ReplaySubject

class UserMemberServiceManager(context: Context) {

    private val repository: UserDataRepository by lazy {
        UserDataRepository(context)
    }
    public var userDataSubject: ReplaySubject<Member> = ReplaySubject.createWithSize(1)
    private var userData: Member? = null
    fun getUserDataFromFireStore() {
        FirebaseVar.user?.let {  fireStoreUser ->
            FirebaseVar.dbFIB?.let { db ->
                db.collection("Members").document(fireStoreUser.email!!)
                    .get()
                    .addOnSuccessListener { documentSnapshot ->
                        documentSnapshot.toObject(Member::class.java)?.let { memberData ->
                            // get document success but no document
                            if (memberData == null) {
                                repository.addUserDataInGlobal(Member(fireStoreUser.displayName!!,fireStoreUser.email!!,"",false))
                            } else {
                                // get document success
                                this.userData = memberData
                                userDataSubject.onNext(memberData)
                                repository.saveUserDataToLocal(memberData)
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        // failed getting document
                    }
            }
        }
    }
    fun addUserDataToFireStore(data: Member) {
        FirebaseVar.user?.let { fireStoreUser ->
            FirebaseVar.dbFIB?.let { db ->
                db.collection("Members").document(fireStoreUser.email!!)
                    .set(data)
                    .addOnSuccessListener {
                        // add document success
                        this.userData = data
                        userDataSubject.onNext(data)
                        repository.saveUserDataToLocal(data)
                    }
                    .addOnFailureListener { exception ->
                        // add document fail
                    }
            }
        }
    }
    fun updateInoutStatusToFireStore(inoutStatus: Boolean) {
        FirebaseVar.user?.let { user ->
            FirebaseVar.dbFIB?.let { db ->
                db.collection("Members").document(user.email!!)
                    .update("inoutStatus",inoutStatus)
                    .addOnSuccessListener {
                        // update inoutStatus success
                        this.userData?.inoutStatus = inoutStatus
                        userDataSubject.onNext(this.userData!!)
                        repository.updateInOutStatusInLocal(inoutStatus)
                    }
                    .addOnFailureListener{ exception ->
                        // update inoutStatus fail
                    }

            }
        }
    }
    fun updatePositionToFireStore(position: String) {
        FirebaseVar.user?.let { user ->
            FirebaseVar.dbFIB?.let { db ->
                db.collection("Members").document(user.email!!)
                    .update("position",position)
                    .addOnSuccessListener {
                        // update position success
                        this.userData?.position = position
                        userDataSubject.onNext(this.userData!!)
                        repository.updatePositionInLocal(position)
                    }
                    .addOnFailureListener { exception ->
                        // update position fail
                    }
            }
        }
    }

}
package com.example.comuse_kotlin.fireStoreService

import androidx.lifecycle.MutableLiveData
import com.example.comuse_kotlin.dataModel.Member

class UserMemberServiceManager {
    private var userData: Member? = null

    fun getUserDataFromFireStore(userMemberData: MutableLiveData<Member>) {
        FirebaseVar.user?.let {  fireStoreUser ->
            FirebaseVar.dbFIB?.let { db ->
                db.collection("Members").document(fireStoreUser.email!!)
                    .get()
                    .addOnSuccessListener { documentSnapshot ->
                        documentSnapshot.toObject(Member::class.java)?.let { memberData ->
                            // get document success
                            userData = memberData
                            userMemberData.postValue(userData)
                        }
                        // get document success, but data is null
                    }
                    .addOnFailureListener { exception ->
                        // failed getting document
                    }
            }
        }
    }
    fun addUserDataToFireStore(userMemberData: MutableLiveData<Member>, data: Member) {
        FirebaseVar.user?.let { fireStoreUser ->
            FirebaseVar.dbFIB?.let { db ->
                db.collection("Members").document(fireStoreUser.email!!)
                    .set(data)
                    .addOnSuccessListener {
                        // add document success
                        userMemberData.postValue(data)
                    }
                    .addOnFailureListener { exception ->
                        // add document fail
                    }
            }
        }
    }
    fun updateInoutStatusToFireStore(userMemberData: MutableLiveData<Member>, inoutStatus: Boolean) {
        FirebaseVar.user?.let { user ->
            FirebaseVar.dbFIB?.let { db ->
                db.collection("Members").document(user.email!!)
                    .update("inoutStatus",inoutStatus)
                    .addOnSuccessListener {
                        // update inoutStatus success
                        userMemberData.postValue(Member(userMemberData.value!!.name,userMemberData.value!!.email,userMemberData.value!!.position,inoutStatus))
                    }
                    .addOnFailureListener{ exception ->
                        // update inoutStatus fail
                    }

            }
        }
    }
    fun updatePositionToFireStore(userMemberData: MutableLiveData<Member>, position: String) {
        FirebaseVar.user?.let { user ->
            FirebaseVar.dbFIB?.let { db ->
                db.collection("Members").document(user.email!!)
                    .update("position",position)
                    .addOnSuccessListener {
                        // update position success
                        userMemberData.postValue(Member(userMemberData.value!!.name,userMemberData.value!!.email,position,userMemberData.value!!.inoutStatus))
                    }
                    .addOnFailureListener { exception ->
                        // update position fail
                    }
            }
        }
    }

}
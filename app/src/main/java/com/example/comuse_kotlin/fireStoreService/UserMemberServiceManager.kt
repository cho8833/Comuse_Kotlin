package com.example.comuse_kotlin.fireStoreService


import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.comuse_kotlin.dataModel.Member
import com.example.comuse_kotlin.repository.UserDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserMemberServiceManager(context: Context) {

    private val repository: UserDataRepository by lazy {
        UserDataRepository(context)
    }
    fun getUserDataFromFireStore(userMemberData: MutableLiveData<Member>) {
        FirebaseVar.user?.let {  fireStoreUser ->
            FirebaseVar.dbFIB?.let { db ->
                db.collection("Members").document(fireStoreUser.email!!)
                    .get()
                    .addOnSuccessListener { documentSnapshot ->
                        documentSnapshot.toObject(Member::class.java)?.let { memberData ->
                            // get document success but no document
                            if (memberData == null) {
                                repository.addUserData(Member(fireStoreUser.displayName!!,fireStoreUser.email!!,"",false))
                            } else {
                                // get document success
                                userMemberData.postValue(memberData)
                                CoroutineScope(Dispatchers.IO).launch {
                                    repository.saveUserDataToLocal(memberData)
                                }
                            }
                        }
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
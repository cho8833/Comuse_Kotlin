package com.example.comuse_kotlin.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.comuse_kotlin.dataModel.Member
import com.example.comuse_kotlin.fireStoreService.UserMemberServiceManager
import com.example.comuse_kotlin.sharedPreferences.UserDataPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserDataRepository(private val context: Context) {
    var userMemberData: MutableLiveData<Member> = MutableLiveData()
    // FireStore Communication Object
    private val userMemberServiceManager: UserMemberServiceManager by lazy {
        UserMemberServiceManager(context)
    }

    // SharedPreferences Communication Object
    private val userDataPreferences: UserDataPreferences by lazy {
        UserDataPreferences(context)
    }

    fun getUserData() {
        // get data from local
        CoroutineScope(Dispatchers.IO).launch {
            val member = userDataPreferences.loadUserData()
            userMemberData.postValue(userDataPreferences.loadUserData())
        }

        // get data from FireStore
        userMemberServiceManager.getUserDataFromFireStore(userMemberData)
    }

    fun updateInOutStatus(inoutStatus: Boolean) {
        // update data in local
        userDataPreferences.updateUserData(inoutStatus,null)
        // update data in global
        userMemberServiceManager.updateInoutStatusToFireStore(userMemberData,inoutStatus)
    }
    fun updatePosition(position: String) {
        // update data in local
        userDataPreferences.updateUserData(null,position)
        // update data in global
        userMemberServiceManager.updatePositionToFireStore(userMemberData,position)
    }
    fun addUserData(member: Member) {
        // add data in local
        userDataPreferences.saveUserData(member)
        // add data in global
        userMemberServiceManager.addUserDataToFireStore(userMemberData,member)
    }
    fun saveUserDataToLocal(member: Member) {
        userDataPreferences.saveUserData(member)
    }

}
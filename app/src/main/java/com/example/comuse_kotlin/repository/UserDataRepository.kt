package com.example.comuse_kotlin.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.comuse_kotlin.dataModel.Member
import com.example.comuse_kotlin.fireStoreService.UserMemberServiceManager
import com.example.comuse_kotlin.sharedPreferences.UserDataPreferences
import io.reactivex.disposables.Disposable
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
    private var disposableGlobal: Disposable? = null
    fun getUserData() {
        // get data from local
        CoroutineScope(Dispatchers.IO).launch {
            userMemberData.postValue(userDataPreferences.loadUserData())
        }

        // get data from FireStore
        userMemberServiceManager.getUserDataFromFireStore()
        disposableGlobal = userMemberServiceManager.userDataSubject.subscribe { userData ->
            userMemberData.postValue(userData)
        }
    }
    fun unsubscribeGlobal() {
        disposableGlobal?.dispose()
    }

    // ************ Global Data Control ************
    fun addUserDataInGlobal(member: Member) {
        userMemberServiceManager.addUserDataToFireStore(member)
    }
    fun updateInOutStatusInGlobal(inoutStatus: Boolean) {
        userMemberServiceManager.updateInoutStatusToFireStore(inoutStatus)
    }
    fun updatePositionInGlobal(position: String) {
        userMemberServiceManager.updatePositionToFireStore(position)
    }

    // ************ Local Data Control ************
    fun saveUserDataToLocal(member: Member) {
        CoroutineScope(Dispatchers.IO).launch {
            userDataPreferences.saveUserData(member)
        }
        userDataPreferences.saveUserData(member)
    }
    fun updateInOutStatusInLocal(inoutStatus: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            userDataPreferences.updateUserData(inoutStatus,null)
        }
    }
    fun updatePositionInLocal(position: String) {
        CoroutineScope(Dispatchers.IO).launch {
            userDataPreferences.updateUserData(null,position)
        }
    }
}
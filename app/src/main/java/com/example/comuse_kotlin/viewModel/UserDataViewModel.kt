package com.example.comuse_kotlin.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.comuse_kotlin.dataModel.Member
import com.example.comuse_kotlin.repository.UserDataRepository

class UserDataViewModel(application: Application): AndroidViewModel(application) {
    public val userDataForView: MutableLiveData<Member> by lazy {
        return@lazy repository.userMemberData
    }
    private val repository: UserDataRepository by lazy {
        UserDataRepository(application.applicationContext)
    }
    fun getUserData() {
        repository.getUserData()
    }
    fun updateInoutStatus(inoutStatus: Boolean) {
        repository.updateInOutStatus(inoutStatus)
    }
    fun updatePosition(position: String) {
        repository.updatePosition(position)
    }
    fun addUserData(member: Member) {
        repository.addUserData(member)
    }
}
package com.example.comuse_kotlin.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.comuse_kotlin.dataModel.Member
import com.example.comuse_kotlin.repository.UserDataRepository

class UserDataViewModel(application: Application): AndroidViewModel(application) {
    private val userMemberData: MutableLiveData<Member> by lazy {
        userDataRepository.getUserData()
        return@lazy userDataRepository.userMemberData
    }
    private val userDataRepository: UserDataRepository by lazy {
        UserDataRepository(application.applicationContext)
    }
    fun getUserData(): MutableLiveData<Member> {
        return userMemberData
    }
    fun updateInoutStatus(inoutStatus: Boolean) {
        userDataRepository.updateInOutStatus(inoutStatus)
    }
    fun updatePosition(position: String) {
        userDataRepository.updatePosition(position)
    }
    fun addUserData(member: Member) {
        userDataRepository.addUserData(member)
    }
}
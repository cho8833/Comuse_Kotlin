package com.example.comuse_kotlin.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.comuse_kotlin.dataModel.Member
import com.example.comuse_kotlin.repository.UserDataRepository

class UserDataViewModel(private val application: Application) {
    private var userMemberData: MutableLiveData<Member> by lazy {

    }
    private val userDataRepository: UserDataRepository by lazy {

    }
    fun getUserData(): MutableLiveData<Member> {
        return userMemberData
    }

}
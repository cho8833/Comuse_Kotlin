package com.example.comuse_kotlin.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.comuse_kotlin.dataModel.Member
import com.example.comuse_kotlin.repository.MembersRepository

class MembersViewModel(application: Application): AndroidViewModel(application) {
    private val members: MutableLiveData<ArrayList<Member>> by lazy {
        membersRepository.getMembers()
    }
    private val membersRepository: MembersRepository by lazy {
        MembersRepository(application)
    }
    fun getAllMembers(): MutableLiveData<ArrayList<Member>> {
        return members
    }
}
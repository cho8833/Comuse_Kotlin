package com.example.comuse_kotlin.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.comuse_kotlin.dataModel.Member
import com.example.comuse_kotlin.repository.MembersRepository

class MembersViewModel(application: Application): AndroidViewModel(application) {

    public val membersForView: MutableLiveData<ArrayList<Member>> by lazy {
        return@lazy repository.members
    }
    private val repository: MembersRepository by lazy {
        MembersRepository(application)
    }
    fun getAllMembers() {
        repository.getAllMembers()
    }
}
package com.example.comuse_kotlin

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.comuse_kotlin.viewModel.MembersViewModel
import java.lang.IllegalArgumentException


class MembersViewModelFactory(private val application: Application): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MembersViewModel::class.java)) {
            return MembersViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
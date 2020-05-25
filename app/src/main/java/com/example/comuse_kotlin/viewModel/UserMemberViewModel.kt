package com.example.comuse_kotlin.viewModel

import androidx.lifecycle.MutableLiveData
import com.example.comuse_kotlin.dataModel.Member
import com.example.comuse_kotlin.fireStoreService.FirebaseVar

class UserMemberViewModel {
    var userMemberData: MutableLiveData<Member> = MutableLiveData()


}
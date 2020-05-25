package com.example.comuse_kotlin.repository

import com.example.comuse_kotlin.fireStoreService.UserMemberServiceManager

class UserDataRepository {
    val userMemberServiceManager: UserMemberServiceManager by lazy {
        UserMemberServiceManager()
    }

}
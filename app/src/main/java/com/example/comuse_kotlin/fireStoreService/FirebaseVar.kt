package com.example.comuse_kotlin.fireStoreService

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class FirebaseVar {
    companion object {
        var dbFIB: FirebaseFirestore? = null
        var user: FirebaseUser? = null
        var memberListener: ListenerRegistration? = null
        var timeTableListener: ListenerRegistration? = null
    }
}
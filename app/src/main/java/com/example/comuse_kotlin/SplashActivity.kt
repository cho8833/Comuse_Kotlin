package com.example.comuse_kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.comuse_kotlin.fireStoreService.FirebaseVar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        FirebaseVar.user = auth.currentUser
        FirebaseVar.dbFIB = FirebaseFirestore.getInstance()
        val runnable = Runnable {
            Thread.sleep(2000)
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        runnable.run()
    }
}

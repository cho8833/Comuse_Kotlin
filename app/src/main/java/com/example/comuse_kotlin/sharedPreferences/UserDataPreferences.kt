package com.example.comuse_kotlin.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import com.example.comuse_kotlin.dataModel.Member

class UserDataPreferences(private val context: Context) {
    var sharedPreferences: SharedPreferences = context.getSharedPreferences("userData",Context.MODE_PRIVATE)

    fun saveUserData(member: Member) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("name",member.name);
        editor.putString("position",member.position)
        editor.putString("email",member.email)
        editor.putBoolean("inoutStatus",member.inoutStatus)
        editor.commit()
    }
    fun loadUserData(): Member {
        val name = sharedPreferences.getString("name","")
        val position = sharedPreferences.getString("position","")
        val email = sharedPreferences.getString("email","")
        val inoutStatus = sharedPreferences.getBoolean("inoutStatus",false)
        return Member(name!!,email!!,position!!,inoutStatus)
    }
    fun removeUserData() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.commit()
    }
    fun updateUserData(inoutStatus: Boolean?, position: String?) {
        val editor = sharedPreferences.edit()
        inoutStatus?.let {
            editor.putBoolean("inoutStatus",it)
        }
        position?.let {
            editor.putString("position",it)
        }
        editor.commit()
    }
}
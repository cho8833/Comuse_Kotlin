package com.example.comuse_kotlin.dataModel


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "member")
data class Member(var name: String = "", @PrimaryKey var email: String = "", var position: String = "", var inoutStatus: Boolean = false) {

}
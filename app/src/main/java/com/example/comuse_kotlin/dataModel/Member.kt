package com.example.comuse_kotlin.dataModel

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "member")
data class Member(var name: String = "", @PrimaryKey var email: String = "", var position: String = "", var inoutStatus: Boolean = false) {

}
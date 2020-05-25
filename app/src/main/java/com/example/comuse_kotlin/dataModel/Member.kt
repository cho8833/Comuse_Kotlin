package com.example.comuse_kotlin.dataModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "member")
class Member {
    var name: String = ""
    @PrimaryKey var email: String = ""
    var position: String = ""
    var inoutStatus: Boolean = false

    constructor(name: String, email: String, position: String, inoutStatus: Boolean) {
        this.name = name
        this.position = position
        this.email = email
        this.inoutStatus = inoutStatus
    }

}
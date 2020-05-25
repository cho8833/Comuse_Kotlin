package com.example.comuse_kotlin.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.comuse_kotlin.dataModel.Member

@Dao
interface MembersDao {
    @Insert
    fun addMembers(members: List<Member>)

    @Query("SELECT * FROM member")
    fun loadMembers(): LiveData<List<Member>>

    @Delete
    fun deleteMember(member: Member)

    @Update
    fun updateMember(member: Member)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMember(member: Member)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMembers(members: List<Member>)
}
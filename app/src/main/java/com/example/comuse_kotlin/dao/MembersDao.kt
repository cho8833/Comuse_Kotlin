package com.example.comuse_kotlin.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.comuse_kotlin.dataModel.Member

@Dao
interface MembersDao {
    @Insert
    suspend fun addMembers(members: List<Member>)

    @Query("SELECT * FROM member")
    suspend fun loadMembers(): List<Member>
    @Delete
    suspend fun deleteMember(member: Member)

    @Update
    suspend fun updateMember(member: Member)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMember(member: Member)

}
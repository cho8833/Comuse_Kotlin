package com.example.comuse_kotlin.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.comuse_kotlin.dao.MembersDao
import com.example.comuse_kotlin.dataModel.Member
import com.example.comuse_kotlin.fireStoreService.MembersServiceManager
import com.example.comuse_kotlin.room.RoomDataBase

class MembersRepository(private val application: Application) {

    // FireStore Communication Object
    private val membersServiceManager = MembersServiceManager(application)

    // Room Communication Object
    private val membersDao: MembersDao by lazy {
        RoomDataBase.getInstance(application)!!.membersDao()
    }

    fun getMembers(): MutableLiveData<ArrayList<Member>> {
        val members: MutableLiveData<ArrayList<Member>> = MutableLiveData()
        val runnable: Runnable = Runnable {
            val membersList = membersDao.loadMembers()
            val membersToArray: ArrayList<Member> = ArrayList()
            membersList.value?.let { membersToArray.addAll(it) }
            members.postValue(membersToArray)
        }
        Thread(runnable).start()
        membersServiceManager.getMembersFromFireStore(members)
        return members
    }

    fun updateMember(update: Member) {
        membersDao.updateMember(update)
    }
    fun addMember(add: Member) {
        membersDao.addMember(add)
    }
    fun removeMember(remove: Member) {
        membersDao.deleteMember(remove)
    }

}
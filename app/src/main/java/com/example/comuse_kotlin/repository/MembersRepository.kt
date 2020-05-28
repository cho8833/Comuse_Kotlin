package com.example.comuse_kotlin.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import com.example.comuse_kotlin.dao.MembersDao
import com.example.comuse_kotlin.dataModel.Member
import com.example.comuse_kotlin.fireStoreService.FirebaseVar
import com.example.comuse_kotlin.fireStoreService.MembersServiceManager
import com.example.comuse_kotlin.fireStoreService.UserMemberServiceManager
import com.example.comuse_kotlin.room.RoomDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MembersRepository(private val application: Application) {

    // FireStore Communication Object
    private val membersServiceManager = MembersServiceManager(application)

    // Room Communication Object
    private val membersDao: MembersDao by lazy {
        RoomDataBase.getInstance(application)!!.membersDao()
    }

    // Members LiveData
    var members: MutableLiveData<ArrayList<Member>> = MutableLiveData()

    fun getAllMembers() {
        // FireStore 의 snapshot 리스너가 활성화 되어잇으면 따로 데이터 가져오기 작업을 하지 않는다.
        if (FirebaseVar.memberListener != null) {
            return
        }
        // FireStore 의 snapshot 리스너가 활성화 되어있지 않을 때 데이터를 local 과 server 에서 가져온다.
        CoroutineScope(Dispatchers.IO).launch {
            var membersArray = ArrayList<Member>()
            membersArray.addAll(membersDao.loadMembers())
            members.postValue(membersArray)
        }
        membersServiceManager.getMembersFromFireStore(members)
    }

    fun addMemberToLocal(member: Member) {
        CoroutineScope(Dispatchers.IO).launch {
            membersDao.addMember(member)
        }
    }
    fun updateMemberToLocal(member: Member) {
        CoroutineScope(Dispatchers.IO).launch {
            membersDao.updateMember(member)
        }
    }
    fun removeMemberToLocal(member: Member) {
        CoroutineScope(Dispatchers.IO).launch {
            membersDao.deleteMember(member)
        }
    }

}
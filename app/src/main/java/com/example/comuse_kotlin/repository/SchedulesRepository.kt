package com.example.comuse_kotlin.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.comuse_kotlin.dao.SchedulesDao
import com.example.comuse_kotlin.dataModel.ScheduleData
import com.example.comuse_kotlin.fireStoreService.SchedulesServiceManager
import com.example.comuse_kotlin.room.RoomDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SchedulesRepository(context: Context) {

    // FireStore Communication Object
    private val schedulesServiceManager: SchedulesServiceManager by lazy {
        SchedulesServiceManager(context)
    }

    // Room Communication Object
    private val schedulesDao: SchedulesDao by lazy {
        RoomDataBase.getInstance(context)!!.schedulesDao()
    }
    var schedules: MutableLiveData<ArrayList<ScheduleData>> = MutableLiveData()

    fun getSchedules() {
        CoroutineScope(Dispatchers.IO).launch {
            var schedulesArray = ArrayList<ScheduleData>()
            schedulesArray.addAll(schedulesDao.loadSchedules())
            schedules.postValue(schedulesArray)

        }
        schedulesServiceManager.getSchedulesFromFireStore(schedules)
    }

}
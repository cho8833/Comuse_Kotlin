package com.example.comuse_kotlin.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.comuse_kotlin.dao.SchedulesDao
import com.example.comuse_kotlin.dataModel.ScheduleData
import com.example.comuse_kotlin.fireStoreService.FirebaseVar
import com.example.comuse_kotlin.fireStoreService.SchedulesServiceManager
import com.example.comuse_kotlin.room.RoomDataBase
import io.reactivex.disposables.Disposable
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
    private var globalDisposable: Disposable? = null
    var schedules: MutableLiveData<ArrayList<ScheduleData>> = MutableLiveData()

    fun getSchedules() {
        //FireStore Listener 가 활성화 되어있으면(null 값이 아니면) 데이터를 받아오지않는다
        if ( FirebaseVar.timeTableListener != null) {
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            var schedulesArray = ArrayList<ScheduleData>()
            schedulesArray.addAll(schedulesDao.loadSchedules())
            schedules.postValue(schedulesArray)
        }
        schedulesServiceManager.clearList()
        schedulesServiceManager.getSchedulesFromFireStore()
        globalDisposable = schedulesServiceManager.schedulesSubject.subscribe { scheduleDataList ->
            schedules.postValue(scheduleDataList)
        }
    }
    fun unsubscribeGlobal() {
        globalDisposable?.dispose()
    }

    // ************ Global Data Control ************
    fun updateSchedule(scheduleData: ScheduleData) {
        schedulesServiceManager.updateScheduleInFireStore(scheduleData)
    }
    fun deleteSchedule(scheduleData: ScheduleData) {
        schedulesServiceManager.deleteScheduleInFireStore(scheduleData)
    }
    fun addSchedule(scheduleData: ScheduleData) {
        schedulesServiceManager.addScheduleToFireStore(scheduleData)
    }

    // ************ Local Data Control ************
    fun addScheduleToLocal(scheduleData: ScheduleData) {
        CoroutineScope(Dispatchers.IO).launch {
            schedulesDao.addSchedule(scheduleData)
        }
    }
    fun updateScheduleToLocal(scheduleData: ScheduleData) {
        CoroutineScope(Dispatchers.IO).launch {
            schedulesDao.updateSchedule(scheduleData)
        }
    }
    fun deleteScheduleToLocal(scheduleData: ScheduleData) {
        CoroutineScope(Dispatchers.IO).launch {
            schedulesDao.deleteSchedule(scheduleData)
        }
    }
}
package com.example.comuse_kotlin.viewModel

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.comuse_kotlin.dataModel.ScheduleData
import com.example.comuse_kotlin.repository.SchedulesRepository

class SchedulesViewModel(application: Application): AndroidViewModel(application) {
    public val schedulesForView: MutableLiveData<ArrayList<ScheduleData>> by lazy {
        return@lazy repository.schedules
    }
    private val repository: SchedulesRepository by lazy {
        SchedulesRepository(application)
    }
    fun getAllSchedules() {
        repository.getSchedules()
    }
    fun updateSchedule(scheduleData: ScheduleData) {
        repository.updateSchedule(scheduleData)
    }
    fun addSchedule(scheduleData: ScheduleData) {
        repository.addSchedule(scheduleData)
    }
}
package com.example.comuse_kotlin.dao

import androidx.room.*
import com.example.comuse_kotlin.dataModel.ScheduleData


@Dao
interface SchedulesDao {
    @Query("SELECT * FROM schedule")
    suspend fun loadSchedules(): List<ScheduleData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSchedule(data: ScheduleData)

    @Update
    suspend fun updateSchedule(data: ScheduleData)

    @Delete
    suspend fun deleteSchedule(data: ScheduleData)
}
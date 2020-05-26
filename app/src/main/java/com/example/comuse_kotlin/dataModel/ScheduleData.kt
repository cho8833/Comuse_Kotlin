package com.example.comuse_kotlin.dataModel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.tlaabs.timetableview.Schedule
@Entity(tableName= "schedule")
data class ScheduleData(var schedule: Schedule = Schedule(), @PrimaryKey var key: String = "") {
}
package com.example.comuse_kotlin.dataModel

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.tlaabs.timetableview.Schedule
import com.github.tlaabs.timetableview.Time
import kotlinx.android.parcel.Parcelize

/*
Class Schedule {
    String classTitle="";
    String classPlace="";
    String professorName="";
    private int day = 0;
    private Time startTime;
    private Time endTime;
}
Class Time {
    private int hour = 0;
    private int minute = 0;
}
 */
@Parcelize
@Entity(tableName= "schedule")
data class ScheduleData(var classTitle: String = "", var classPlace: String = "", var professorName: String = "", var day: Int = 0,
                        var startTimeHour: Int = 0, var startTimeMinute: Int = 0, var endTimeHour: Int = 0, var endTimeMinute: Int = 0, @PrimaryKey var key: String = "") :
    Parcelable {
    companion object {
        fun dataToSchedule(scheduleData: ScheduleData): Schedule {
            var schedule = Schedule()
            schedule.startTime = Time(scheduleData.startTimeHour,scheduleData.startTimeMinute)
            schedule.endTime   = Time(scheduleData.endTimeHour, scheduleData.endTimeMinute)
            schedule.classTitle = scheduleData.classTitle
            schedule.classPlace = ""
            schedule.professorName = scheduleData.professorName
            schedule.day = scheduleData.day
            return schedule
        }

    }
}
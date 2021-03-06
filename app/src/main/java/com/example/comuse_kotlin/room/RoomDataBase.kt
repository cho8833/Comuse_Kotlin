package com.example.comuse_kotlin.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.comuse_kotlin.dao.MembersDao
import com.example.comuse_kotlin.dao.SchedulesDao
import com.example.comuse_kotlin.dataModel.Member
import com.example.comuse_kotlin.dataModel.ScheduleData

@Database(entities = [Member::class, ScheduleData::class], version = 4, exportSchema = false)
abstract class RoomDataBase: RoomDatabase() {
    abstract fun membersDao(): MembersDao
    abstract  fun schedulesDao(): SchedulesDao
    companion object {
        private var instance: RoomDataBase? = null

        fun getInstance(context: Context): RoomDataBase? {
            return instance?: synchronized(RoomDataBase::class) {
                instance?: Room.databaseBuilder(context.applicationContext,
                    RoomDataBase::class.java, "liveData-db")
                    .fallbackToDestructiveMigration()
                    .build().also {
                    instance = it
                }
            }

        }

        fun destroyInstance() {
            instance = null
        }

    }
}
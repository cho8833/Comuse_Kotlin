package com.example.comuse_kotlin.fireStoreService

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.comuse_kotlin.dataModel.ScheduleData
import com.example.comuse_kotlin.repository.SchedulesRepository
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SchedulesServiceManager(context: Context) {
    private var schedulesList: ArrayList<ScheduleData> = ArrayList()
    private val repository: SchedulesRepository = SchedulesRepository(context)
    fun getSchedulesFromFireStore(schedules: MutableLiveData<ArrayList<ScheduleData>>) {
        FirebaseVar.user?.let { _ ->
            FirebaseVar.dbFIB?.let { db ->
                FirebaseVar.timeTableListener = db.collection("TimeTable")
                    .addSnapshotListener { querySnapshot, e ->
                        if (e != null) {
                            Log.d(ContentValues.TAG, "SnapShot Listen Error", e)
                            return@addSnapshotListener
                        }
                        if (querySnapshot != null && !querySnapshot.isEmpty) {
                            for (documentChange in querySnapshot.documentChanges) {
                                var scheduleData = documentChange.document.toObject(ScheduleData::class.java)
                                when (documentChange.type) {
                                    DocumentChange.Type.ADDED -> {
                                        schedulesList.add(scheduleData)
                                        CoroutineScope(Dispatchers.IO).launch { repository.addScheduleToLocal(scheduleData) }

                                    }
                                    DocumentChange.Type.MODIFIED -> {
                                        val index = schedulesList.lastIndexOf(scheduleData)
                                        schedulesList.removeAt(index)
                                        schedulesList.add(index,scheduleData)
                                        CoroutineScope(Dispatchers.IO).launch { repository.updateScheduleToLocal(scheduleData) }

                                    }
                                    DocumentChange.Type.REMOVED -> {
                                        schedulesList.removeAt(schedulesList.lastIndexOf(scheduleData))
                                        CoroutineScope(Dispatchers.IO).launch { repository.deleteScheduleToLocal(scheduleData) }

                                    }
                                }
                            }
                            // post value
                            schedules.postValue(schedulesList)
                        }
                    }
            }
        }
    }
    fun updateScheduleInFireStore(scheduleData: ScheduleData) {
        FirebaseVar.user?.let { _ ->
            FirebaseVar.dbFIB?.let { db ->
                db.collection("TimeTable").document(scheduleData.key)
                    .update(mapOf(
                        "classTitle" to scheduleData.classTitle,
                        "professorName" to scheduleData.professorName,
                        "day" to scheduleData.day,
                        "startTimeHour" to scheduleData.startTimeHour,
                        "startTimeMinute" to scheduleData.startTimeMinute,
                        "endTimeHour" to scheduleData.endTimeHour,
                        "endTimeMinute" to scheduleData.endTimeMinute))
            }
        }
    }
    fun deleteScheduleInFireStore(scheduleData: ScheduleData) {
        FirebaseVar.user?.let { _ ->
            FirebaseVar.dbFIB?.let { db ->
                db.collection("TimeTable").document(scheduleData.key)
                    .delete()
            }
        }
    }
    fun addScheduleToFireStore(scheduleData: ScheduleData) {
        FirebaseVar.user?.let { _ ->
            FirebaseVar.dbFIB?.let { db ->
                db.collection("TimeTable").document(scheduleData.key)
                    .set(scheduleData)
            }
        }
    }
}
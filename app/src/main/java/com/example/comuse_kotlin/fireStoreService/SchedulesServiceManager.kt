package com.example.comuse_kotlin.fireStoreService

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.comuse_kotlin.dataModel.ScheduleData
import com.example.comuse_kotlin.repository.SchedulesRepository
import com.google.firebase.firestore.DocumentChange
import io.reactivex.subjects.ReplaySubject

class SchedulesServiceManager(context: Context) {
    private var schedulesList: ArrayList<ScheduleData> = ArrayList()
    private val repository: SchedulesRepository = SchedulesRepository(context)
    public var schedulesSubject: ReplaySubject<ArrayList<ScheduleData>> = ReplaySubject.createWithSize(20)

    fun getSchedulesFromFireStore() {
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
                                Log.d("data","document ="+documentChange.document.data+documentChange.type)
                                var scheduleData = documentChange.document.toObject(ScheduleData::class.java)
                                when (documentChange.type) {
                                    DocumentChange.Type.ADDED -> {
                                        schedulesList.add(scheduleData)
                                        repository.addScheduleToLocal(scheduleData)

                                    }
                                    DocumentChange.Type.MODIFIED -> {
                                        for(compare: ScheduleData in schedulesList) {
                                            if ( compare.key == scheduleData.key) {
                                                var index = schedulesList.indexOf(compare)
                                                schedulesList.removeAt(index)
                                                schedulesList.add(index, scheduleData)
                                                repository.updateScheduleToLocal(scheduleData)
                                                break;
                                            }
                                        }
                                    }
                                    DocumentChange.Type.REMOVED -> {
                                        schedulesList.removeAt(schedulesList.lastIndexOf(scheduleData))
                                        repository.deleteScheduleToLocal(scheduleData)

                                    }
                                }
                            }
                            // post value
                            schedulesSubject.onNext(schedulesList)
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
    fun clearList() {
        this.schedulesList.clear()
    }
}
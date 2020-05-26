package com.example.comuse_kotlin.fireStoreService

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.comuse_kotlin.dataModel.ScheduleData
import com.example.comuse_kotlin.repository.SchedulesRepository
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener

class SchedulesServiceManager(context: Context) {
    private var schedulesList: ArrayList<ScheduleData> = ArrayList()

    fun getSchedulesFromFireStore(schedules: MutableLiveData<ArrayList<ScheduleData>>) {
        FirebaseVar.user?.let { _ ->
            FirebaseVar.dbFIB?.let { db ->
                db.collection("TimeTable")
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
                                    }
                                    DocumentChange.Type.MODIFIED -> {
                                        val index = schedulesList.lastIndexOf(scheduleData)
                                        schedulesList.removeAt(index)
                                        schedulesList.add(index,scheduleData)
                                    }
                                    DocumentChange.Type.REMOVED -> {
                                        schedulesList.removeAt(schedulesList.lastIndexOf(scheduleData))
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
                    .update(scheduleData)
                    .
            }
        }
    }
}
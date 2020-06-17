package com.example.comuse_kotlin.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.comuse_kotlin.Edit_AddScheduleActivity
import com.example.comuse_kotlin.MainActivity
import com.example.comuse_kotlin.R
import com.example.comuse_kotlin.dataModel.ScheduleData
import com.example.comuse_kotlin.databinding.FragmentTimeTableBinding
import com.example.comuse_kotlin.fireStoreService.FirebaseVar
import com.example.comuse_kotlin.viewModel.SchedulesViewModel
import com.github.tlaabs.timetableview.Schedule
import com.google.firebase.auth.FirebaseAuth


class TimeTableFragment : Fragment() {
    private lateinit var binding: FragmentTimeTableBinding
    private lateinit var schedulesViewModel: SchedulesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory: ViewModelProvider.Factory = ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        schedulesViewModel = ViewModelProvider(activity as ViewModelStoreOwner, factory).get(SchedulesViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_time_table,container,false)

        //bind data to TimeTable
        bindSchedules()

        // check user signed in
        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            auth.currentUser?.let { user ->
                // signed in
                schedulesViewModel.getAllSchedules()
                setScheduleAddButtonClickListener()
                return@addAuthStateListener
            }
            // signed out
            FirebaseVar.timeTableListener?.remove()
            schedulesViewModel.schedulesForView.postValue(ArrayList())
        }


        return binding.root
    }
    private fun bindSchedules() {
        schedulesViewModel.schedulesForView.observe(activity as LifecycleOwner, Observer {
            binding.timetable.removeAll()
            for(scheduleData: ScheduleData in it) {
                var scheduleArray = ArrayList<Schedule>()
                scheduleArray.add(ScheduleData.dataToSchedule(scheduleData))
                binding.timetable.add(scheduleArray)
            }
        })
    }
    private fun setScheduleAddButtonClickListener() {
        // start Edit_AddScheduleActivity
        binding.addScheduleBtn.setOnClickListener {
            val intent = Intent(context,Edit_AddScheduleActivity::class.java)
            startActivity(intent)
        }

    }

}

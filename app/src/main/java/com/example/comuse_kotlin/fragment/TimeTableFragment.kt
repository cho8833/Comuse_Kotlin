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
import com.example.comuse_kotlin.R
import com.example.comuse_kotlin.dataModel.ScheduleData
import com.example.comuse_kotlin.databinding.FragmentTimeTableBinding
import com.example.comuse_kotlin.viewModel.SchedulesViewModel
import com.github.tlaabs.timetableview.Schedule
import com.github.tlaabs.timetableview.Time
import com.github.tlaabs.timetableview.TimetableView


class TimeTableFragment : Fragment() {
    private lateinit var binding: FragmentTimeTableBinding
    private lateinit var schedulesViewModel: SchedulesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory: ViewModelProvider.Factory = ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        schedulesViewModel = ViewModelProvider((context as ViewModelStoreOwner?)!!, factory).get(SchedulesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_time_table,container,false)

        // draw TimeTable
        schedulesViewModel.getAllSchedules().observe(context as LifecycleOwner, Observer {
            binding.timetable.removeAll()
            for(scheduleData: ScheduleData in it) {
                var scheduleArray = ArrayList<Schedule>()
                scheduleArray.add(ScheduleData.dataToSchedule(scheduleData))
                binding.timetable.add(scheduleArray)
            }

        })

        // add/edit ScheduleButton
        binding.addScheduleBtn.setOnClickListener {
            // start Edit/add Schedule Activity
            val intent = Intent(context,Edit_AddScheduleActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

}

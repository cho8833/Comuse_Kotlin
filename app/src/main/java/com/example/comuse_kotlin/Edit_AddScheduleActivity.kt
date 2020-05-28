package com.example.comuse_kotlin

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.NumberPicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.comuse_kotlin.dataModel.ScheduleData
import com.example.comuse_kotlin.databinding.ActivityEditAddScheduleBinding
import com.example.comuse_kotlin.fireStoreService.FirebaseVar
import com.example.comuse_kotlin.viewModel.SchedulesViewModel
import java.util.*
import kotlin.random.Random


class Edit_AddScheduleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditAddScheduleBinding
    private lateinit var schedulesViewModel: SchedulesViewModel
    private var dayIndex: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_add_schedule)
        val scheduleData_get = intent.getParcelableExtra<ScheduleData>("get")

        // get Schedules for Compare
        val factory: ViewModelProvider.Factory =
            ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        val provider = ViewModelProvider(this, factory)
        schedulesViewModel = provider.get(SchedulesViewModel::class.java)

        //spinner settings
        val spinnerItems = resources.getStringArray(R.array.spinner_day)
        val spinnerAdapter =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, spinnerItems)
        binding.spinner.adapter = spinnerAdapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                dayIndex = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        //timePicker settings
        binding.timePickerStart.setIs24HourView(true)
        binding.timePickerEnd.setIs24HourView(true)
        setMinMaxTime(binding.timePickerStart)
        setMinMaxTime(binding.timePickerEnd)
        setTimePickerInterval(binding.timePickerStart)
        setTimePickerInterval(binding.timePickerEnd)
        setStartTimePickerOnChanged()
        setEndTimePickerOnChanged()

        //cancel button settings
        binding.editTimeCancel.setOnClickListener {
            finish()
        }
        if (scheduleData_get != null) {
            editUI(scheduleData_get)
        } else {
            addUI()
        }

    }


    private fun editUI(get: ScheduleData) {
        binding.editTitle.setText(get.classTitle)
        binding.spinner.setSelection(get.day)
        binding.timePickerStart.hour = get.startTimeHour
        binding.timePickerStart.minute = get.startTimeMinute
        binding.timePickerEnd.hour = get.endTimeHour
        binding.timePickerEnd.minute = get.endTimeMinute

        binding.editTimeConfirm.setOnClickListener {
            val title = binding.editTitle.text.toString()
            val startHour = binding.timePickerStart.hour
            val startMin = binding.timePickerStart.minute
            val endHour = binding.timePickerEnd.hour
            val endMin = binding.timePickerEnd.minute
            val day = binding.spinner.selectedItemPosition
            if (checkTimeValid(startHour, startMin, endHour, endMin, day, title)) {
                val scheduleData = ScheduleData(
                    title,
                    "",
                    get.professorName,
                    day,
                    startHour,
                    startMin,
                    endHour,
                    endMin,
                    get.key
                )
                schedulesViewModel.updateSchedule(scheduleData)
                finish()
            } else {
                // notify time invalid
                return@setOnClickListener
            }
        }
    }

    private fun addUI() {
        binding.timePickerStart.hour = 9
        binding.timePickerEnd.hour = 9

        binding.editTimeConfirm.setOnClickListener {
            val title = binding.editTitle.text.toString()
            val startHour = binding.timePickerStart.hour
            val startMin = binding.timePickerStart.minute
            val endHour = binding.timePickerEnd.hour
            val endMin = binding.timePickerEnd.minute
            val day = binding.spinner.selectedItemPosition
            if (checkTimeValid(startHour, startMin, endHour, endMin, day, title)) {
                val scheduleData = ScheduleData(
                    title,
                    "",
                    FirebaseVar.user!!.email!!,
                    day,
                    startHour,
                    startMin,
                    endHour,
                    endMin,
                    generateRandomKey()
                )
                schedulesViewModel.addSchedule(scheduleData)
                finish()
            } else {
                // notify time invalid
                return@setOnClickListener
            }
        }
    }

    private fun checkTimeValid(
        start_hour: Int,
        start_min: Int,
        end_hour: Int,
        end_min: Int,
        day: Int,
        title: String
    ): Boolean {
        if (title.isEmpty()) {
            return false
        }
        // 시작 시간과 종료 시간이 알맞은지 검사
        if (start_hour > end_hour || start_hour == end_hour && start_min >= end_min) {
            return false
        }

        // 다른 scheduleData 와 비교하여 겹치면 edit 불가
        schedulesViewModel.getAllSchedules().value?.let { scheduleDatas ->
            for (scheduleData in scheduleDatas) {
                if (scheduleData.day == day) { /* editData 의 endTime 과 비교데이터의 startTime 비교
                         editData 의 startTime 과 비교데이터의 endTime 비교 */
                    if (scheduleData.startTimeHour == end_hour && scheduleData.endTimeMinute < end_min ||
                        scheduleData.endTimeHour == start_hour && scheduleData.endTimeMinute > start_min
                    ) {
                        return false
                    }
                    if (scheduleData.endTimeHour < end_hour || scheduleData.startTimeHour > start_hour) {
                        return false
                    }
                }
            }
            return true
        }
        return true
    }

    // timePicker 의 분 간격을 30분으로 설정
    private fun setTimePickerInterval(timePicker: TimePicker) {
        try {
            val TIME_PICKER_INTERVAL = 30
            val minutePicker = timePicker.findViewById<NumberPicker>(
                Resources.getSystem().getIdentifier("minute", "id", "android"))
            minutePicker.minValue = 0
            minutePicker.maxValue = 60 / TIME_PICKER_INTERVAL - 1
            val displayedValues: MutableList<String> =
                ArrayList()
            var i = 0
            while (i < 60) {
                displayedValues.add(String.format("%02d", i))
                i += TIME_PICKER_INTERVAL
            }
            minutePicker.displayedValues = displayedValues.toTypedArray()
        } catch (e: Exception) { }
    }

    // timePicker 의 max hour, min hour 설정
    private fun setMinMaxTime(timePicker: TimePicker) {
        try {
            val MIN_TIME = 9
            val MAX_TIME = 24
            val hourPicker = timePicker.findViewById<NumberPicker>(Resources.getSystem().getIdentifier("hour","id","android"))
            hourPicker.minValue = MIN_TIME
            hourPicker.maxValue = MAX_TIME
            val displayedValues: MutableList<String> = ArrayList()
            var i = 9
            while(i<=MAX_TIME) {
                displayedValues.add(String.format("%02d",i))
                i += 1
            }
            hourPicker.displayedValues = displayedValues.toTypedArray()
        } catch (e: java.lang.Exception) {}
    }
    // startTime 이 endTime 을 넘지 않게 유지
    private fun setStartTimePickerOnChanged() {
        binding.timePickerStart.setOnTimeChangedListener { view, hourOfDay, minute ->
            val endHour = binding.timePickerEnd.hour
            if (hourOfDay > endHour) {
                binding.timePickerEnd.hour = hourOfDay + 1
            } else if (hourOfDay == endHour) {
                if (minute >= binding.timePickerEnd.minute) {
                    view.hour = hourOfDay + 1
                }
            }
        }
    }
    private fun setEndTimePickerOnChanged() {
        binding.timePickerEnd.setOnTimeChangedListener { view, hourOfDay, minute ->
            val startHour = binding.timePickerStart.hour
            if (hourOfDay < startHour) {
                binding.timePickerStart.hour = hourOfDay - 1
            } else if (hourOfDay == startHour) {
                if (minute <= binding.timePickerStart.minute) {
                    view.hour = hourOfDay - 1
                }
            }
        }
    }

    private fun generateRandomKey(): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..20)
            .map { i -> Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
}

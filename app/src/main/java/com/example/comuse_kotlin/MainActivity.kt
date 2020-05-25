package com.example.comuse_kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.comuse_kotlin.databinding.ActivityMainBinding
import com.example.comuse_kotlin.viewModel.MembersViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var dataBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        // create ViewModelFactory
        val memberFactory= MembersViewModelFactory(application)
        memberFactory.create(MembersViewModel::class.java)


        // BottomNavigationView Settings
        val fragmentManager: FragmentManager = supportFragmentManager
        val membersFragment = MembersFragment()
        val timeTableFragment = TimeTableFragment()
        val settingsFragment = SettingsFragment()
        fragmentManager.beginTransaction().replace(R.id.frameLayout,membersFragment,membersFragment.javaClass.simpleName).commit()
        dataBinding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            val transaction = fragmentManager.beginTransaction()
            when (item.itemId) {
                R.id.bottomMenuItem_Member -> transaction.replace(R.id.frameLayout,membersFragment).commit()
                R.id.bottomMenuItem_TimeTable -> transaction.replace(R.id.frameLayout,timeTableFragment).commit()
                R.id.bottomMenuItem_Settings -> transaction.replace(R.id.frameLayout,settingsFragment).commit()
            }
            return@setOnNavigationItemSelectedListener true
        }
    }
}

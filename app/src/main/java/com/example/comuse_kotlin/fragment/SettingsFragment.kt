package com.example.comuse_kotlin.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.comuse_kotlin.R
import com.example.comuse_kotlin.SignInActivity
import com.example.comuse_kotlin.databinding.FragmentSettingsBinding

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding = DataBindingUtil.inflate<FragmentSettingsBinding>(inflater,
            R.layout.fragment_settings,container,false)
        binding.button.setOnClickListener {
            val intent = Intent(this.context,
                SignInActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

}

package com.example.comuse_kotlin.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.comuse_kotlin.R
import com.example.comuse_kotlin.SignInActivity
import com.example.comuse_kotlin.databinding.FragmentSettingsBinding
import com.example.comuse_kotlin.fireStoreService.FirebaseVar
import com.example.comuse_kotlin.viewModel.UserDataViewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {
    lateinit var userDataViewModel: UserDataViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // get ViewModel
        val factory: ViewModelProvider.Factory = ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        userDataViewModel = ViewModelProvider(activity as ViewModelStoreOwner, factory).get(
            UserDataViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding = DataBindingUtil.inflate<FragmentSettingsBinding>(inflater,
            R.layout.fragment_settings,container,false)

        // observer UserData LiveData
        userDataViewModel.getUserData().observe(activity as ViewModelStoreOwner as LifecycleOwner, Observer {
            binding.userData = it
        })

        // init signIn/Out button
        binding.buttonSignInout.setOnClickListener(View.OnClickListener {
            FirebaseVar.user?.let {
                // Sign Out
                signOut()
                return@OnClickListener
            }
            // Sign In
            val intent = Intent(this.context, SignInActivity::class.java)
            startActivity(intent)
        })

        // init edit Position Button
        binding.buttonPositionEdit.setOnClickListener(View.OnClickListener {
            FirebaseVar.user?.let { _ ->
                FirebaseVar.dbFIB?.let { _ ->
                    initPositionEditTextDialog().create().show()
                }
            }
        })
        return binding.root
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        FirebaseVar.user = null
        FirebaseVar.dbFIB = null
        FirebaseVar.memberListener = null
        FirebaseVar.timeTableListener = null
    }
    private fun initPositionEditTextDialog(): AlertDialog.Builder {
        val editText = EditText(context)
        val builder = AlertDialog.Builder(context)
        builder.setTitle("포지션 변경")
        builder.setMessage("변경할 포지션을 입력하세요")

        //editText Layout Params Setting
        val container = FrameLayout(context!!)
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.leftMargin =
            resources.getDimensionPixelSize(R.dimen.alert_dialog_internal_margin)
        params.rightMargin =
            resources.getDimensionPixelSize(R.dimen.alert_dialog_internal_margin)
        editText.layoutParams = params
        container.addView(editText)
        builder.setView(container)

        //editText EditButton Setting
        builder.setPositiveButton(
            "Edit"
        ) { _, _ ->
            val position = editText.text.toString()
            userDataViewModel.updatePosition(position)
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.dismiss() }
        return builder
    }
}

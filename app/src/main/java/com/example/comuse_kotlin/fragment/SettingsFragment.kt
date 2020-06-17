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
import com.example.comuse_kotlin.dataModel.Member
import com.example.comuse_kotlin.databinding.FragmentSettingsBinding
import com.example.comuse_kotlin.fireStoreService.FirebaseVar
import com.example.comuse_kotlin.viewModel.UserDataViewModel
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment() {

    private lateinit var userDataViewModel: UserDataViewModel
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // get ViewModel
        val factory: ViewModelProvider.Factory = ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        userDataViewModel = ViewModelProvider(activity as ViewModelStoreOwner, factory).get(UserDataViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_settings,container,false)

        // bind data
        bindUserInfo()

        // check user signed in
        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            auth.currentUser?.let { _ ->
                // signed in
                userDataViewModel.getUserData()
                setSignOutButtonClickListener()
                setPositionEditButtonClickListener()
                return@addAuthStateListener
            }
            // signed out
            userDataViewModel.userDataForView.postValue(Member())
            setSignInButtonClickListener()
        }


        return binding.root
    }

    private fun bindUserInfo() {
        userDataViewModel.userDataForView.observe(activity as ViewModelStoreOwner as LifecycleOwner, Observer { member ->
            binding.userData = member
        })
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        FirebaseVar.user = null
        FirebaseVar.dbFIB = null

    }
    private fun setPositionEditButtonClickListener() {
        binding.buttonPositionEdit.setOnClickListener {
            initPositionEditTextDialog().create().show()
        }
    }
    private fun setSignInButtonClickListener() {
        binding.buttonSignInout.text = "Sign In"
        binding.buttonSignInout.setOnClickListener {
            val intent = Intent(this.context, SignInActivity::class.java)
            startActivity(intent)
        }
    }
    private fun setSignOutButtonClickListener() {
        binding.buttonSignInout.text = "Sign Out"
        binding.buttonSignInout.setOnClickListener {
            signOut()
        }
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

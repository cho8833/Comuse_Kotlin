package com.example.comuse_kotlin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.comuse_kotlin.MemberRecyclerViewAdapter
import com.example.comuse_kotlin.dataModel.Member
import com.example.comuse_kotlin.databinding.FragmentMembersBinding
import com.example.comuse_kotlin.fireStoreService.FirebaseVar
import com.example.comuse_kotlin.viewModel.MembersViewModel
import com.example.comuse_kotlin.viewModel.UserDataViewModel
import com.google.firebase.auth.FirebaseAuth


class MembersFragment : Fragment() {
    private lateinit var membersViewModel: MembersViewModel
    private lateinit var userDataViewModel: UserDataViewModel
    lateinit var binding: FragmentMembersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //MainActivity 에서 만든 ViewModel 을 받아옴
        val factory: ViewModelProvider.Factory = ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        membersViewModel = ViewModelProvider(activity as ViewModelStoreOwner, factory).get(MembersViewModel::class.java)
        userDataViewModel = ViewModelProvider(activity as ViewModelStoreOwner, factory).get(UserDataViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMembersBinding.inflate(inflater,container,false)

        // recyclerview settings
        binding.membersRecyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.membersRecyclerView.adapter = MemberRecyclerViewAdapter(this.context!!)

        // bind data
        bindMembers()
        bindUserInfo()

        // check user signed in
        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            auth.currentUser?.let { _ ->
                // signed in
                membersViewModel.getAllMembers()
                userDataViewModel.getUserData()
                return@addAuthStateListener
            }
            // signed out
            FirebaseVar.memberListener?.remove()
            membersViewModel.membersForView.postValue(ArrayList())
            userDataViewModel.userDataForView.postValue(Member())
        }


        // Inflate the layout for this fragment
        return binding.root
    }

    private fun bindMembers() {
        membersViewModel.membersForView.observe(activity as LifecycleOwner, Observer {
            var adapter = binding.membersRecyclerView.adapter as MemberRecyclerViewAdapter
            if (adapter != null) {
                adapter.updateMemberItemsList(it)
            }
        })
    }
    private fun bindUserInfo() {
        userDataViewModel.userDataForView.observe(activity as LifecycleOwner, Observer {
            updateUserInfo(it)
        })
    }
    private fun updateUserInfo(member: Member) {
        binding.userMemberData = member
        if (member.inoutStatus) { binding.buttonInOut.text = "in" }
        else { binding.buttonInOut.text = "out" }
    }
}

package com.example.comuse_kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.comuse_kotlin.databinding.FragmentMembersBinding
import com.example.comuse_kotlin.viewModel.MembersViewModel

/**
 * A simple [Fragment] subclass.
 */
class MembersFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding = FragmentMembersBinding.inflate(inflater,container,false)
        val membersViewModel = MembersViewModel(activity!!.application)
        binding.membersRecyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.membersRecyclerView.adapter = MemberRecyclerViewAdapter()
        membersViewModel.getAllMembers().observe(this.context as LifecycleOwner, Observer {
            var adapter = binding.membersRecyclerView.adapter as MemberRecyclerViewAdapter
            adapter.setMembersList(it)
            adapter.notifyDataSetChanged()
        })
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_members, container, false)
    }

}

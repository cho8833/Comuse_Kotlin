package com.example.comuse_kotlin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.comuse_kotlin.dataModel.Member
import com.example.comuse_kotlin.databinding.MemberItemLayoutBinding

class MemberRecyclerViewAdapter: RecyclerView.Adapter<MemberRecyclerViewAdapter.ViewHolder>() {
    var members: ArrayList<Member> = ArrayList()

    public fun setMembersList(members: ArrayList<Member>) {
        this.members = members
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MemberItemLayoutBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return members.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(members[position])
    }
    inner class ViewHolder(private var binding: MemberItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(item: Member) {
            binding.memberName.text = item.name
            binding.memberPosition.text = item.position
            if (item.inoutStatus) { binding.memberInoutStatus.text = "in" }
            else { binding.memberInoutStatus.text = "out" }
        }

    }
}
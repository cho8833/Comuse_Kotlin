package com.example.comuse_kotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.comuse_kotlin.dataModel.Member
import com.example.comuse_kotlin.databinding.MemberItemLayoutBinding


class MemberRecyclerViewAdapter(private var context: Context): RecyclerView.Adapter<MemberRecyclerViewAdapter.ViewHolder>() {
    var members: ArrayList<Member> = ArrayList()

    // update recyclerView
    public fun updateMemberItemsList(newList: ArrayList<Member>) {
        val memberDiffCallback = MemberDataDiffUtil(members,newList)
        val diffResult = DiffUtil.calculateDiff(memberDiffCallback)
        members.clear()
        members.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MemberItemLayoutBinding.inflate(LayoutInflater.from(context)))
    }

    override fun getItemCount(): Int {
        return members.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(members[position])
    }
    inner class ViewHolder(private var binding: MemberItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(item: Member) {
            binding.apply {
                member = item
                if (item.inoutStatus) { memberInoutStatus.text = "in" }
                else { memberInoutStatus.text = "out" }
            }
        }

    }
    inner class MemberDataDiffUtil(private val oldList: ArrayList<Member>, private val newList: ArrayList<Member>): DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val (name1, email1, position1, inoutStatus1) = oldList[oldItemPosition]
            val (name2, email2, position2, inoutStatus2) = newList[newItemPosition]
            return name1 == name2 && email1 == email2 && position1 == position2 && inoutStatus1 == inoutStatus2
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            return super.getChangePayload(oldItemPosition, newItemPosition)
        }
    }
}
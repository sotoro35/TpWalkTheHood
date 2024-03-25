package com.hsr2024.tpwalkthehood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.data.CommentItem
import com.hsr2024.tpwalkthehood.databinding.RecyclerviewCommentBinding

class commentAdapter(val context: Context, var list: List<CommentItem>):Adapter<commentAdapter.VHcomment>() {

    inner class VHcomment(val binding:RecyclerviewCommentBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHcomment {
        return VHcomment(RecyclerviewCommentBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VHcomment, position: Int) {
        var item = list[position]

        holder.binding.commentNickname.text = item.nickname
        holder.binding.commentText.text = item.text
        holder.binding.commentDate.text = item.date

        if (item.profile == "1" || item.profile == null){
            holder.binding.commentProfile.setImageResource(R.drawable.profile)
        }else holder.binding.commentProfile.setImageResource(R.drawable.heart_select)

    }
}
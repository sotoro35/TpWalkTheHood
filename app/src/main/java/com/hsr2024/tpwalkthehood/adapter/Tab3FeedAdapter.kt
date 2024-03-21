package com.hsr2024.tpwalkthehood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.hsr2024.tpwalkthehood.G
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.data.FeedItem
import com.hsr2024.tpwalkthehood.databinding.RecyclerviewFeedBinding


class Tab3FeedAdapter(val context: Context, val items:List<FeedItem>):Adapter<Tab3FeedAdapter.VHtabFeed>() {
    inner class VHtabFeed(val binding: RecyclerviewFeedBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHtabFeed {
        return VHtabFeed(RecyclerviewFeedBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VHtabFeed, position: Int) {
        var item = items[position]

        var imgUrl= "http://ruaris.dothome.co.kr/WalkTheHood/${item.profile}"
        holder.binding.tvNickname.text = item.nickname
        holder.binding.tvTitle.text = item.title

        if (item.profile != null){
            Glide.with(context).load(imgUrl).into(holder.binding.ivProfile)
        } else if (item.profile.equals("")) holder.binding.ivProfile.setImageResource(R.drawable.profile)

        if (item.downloadUrl != null && item.downloadUrl !== "") {
            Glide.with(context).load(item.downloadUrl).into(holder.binding.ivFeed)
        } else {
            holder.binding.ivFeed.setImageResource(R.drawable.logotitle)
        }

    }
}
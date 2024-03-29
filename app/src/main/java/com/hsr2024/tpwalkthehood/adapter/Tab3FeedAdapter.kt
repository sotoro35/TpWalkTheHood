package com.hsr2024.tpwalkthehood.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.hsr2024.tpwalkthehood.FeedString
import com.hsr2024.tpwalkthehood.G
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.data.FeedItem
import com.hsr2024.tpwalkthehood.databinding.RecyclerviewFeedBinding
import com.hsr2024.tpwalkthehood.tab3.FeedDetailActivity

// tab3 동네이야기 리사이클러뷰

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
        holder.binding.tvLikenum.text = item.likeNum.toString()

        if (item.profile.equals("1") || item.profile == "") {
            holder.binding.ivProfile.setImageResource(R.drawable.profile)
        }else if (item.profile != null){
            Glide.with(context).load(imgUrl).into(holder.binding.ivProfile)
        }else holder.binding.ivProfile.setImageResource(R.drawable.profile)


        if (item.downUrl == "1" || item.downUrl == ""){
            holder.binding.cardView.visibility = View.GONE
            holder.binding.ivFeed.visibility = View.GONE
        }else if (item.downUrl != null){
            Glide.with(context).load(item.downUrl)
                .into(holder.binding.ivFeed)
        } else {
            holder.binding.cardView.visibility = View.GONE
            holder.binding.ivFeed.visibility = View.GONE
        }

        holder.binding.root.setOnClickListener {
            FeedString.email = item.email
            FeedString.nickname = item.nickname?: ""
            FeedString.profile = item.profile?: ""
            FeedString.title = item.title
            FeedString.text = item.text
            FeedString.date = item.date
            FeedString.downUrl = item.downUrl?: ""
            FeedString.fileName = item.fileName
            FeedString.documentId = item.documentId
            FeedString.likeNum = item.likeNum.toString()

            val intent = Intent(context,FeedDetailActivity::class.java)
            context.startActivity(intent)
        }

    }
}
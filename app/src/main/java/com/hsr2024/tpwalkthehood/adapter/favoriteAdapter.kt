package com.hsr2024.tpwalkthehood.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.gson.Gson
import com.hsr2024.tpwalkthehood.data.favoriteItem
import com.hsr2024.tpwalkthehood.databinding.RecyclerviewFavorlistBinding
import com.hsr2024.tpwalkthehood.tab1.ItemDetailActivity

class favoriteAdapter(val context: Context, val items:List<favoriteItem>):Adapter<favoriteAdapter.VHfavor>(){
    inner class VHfavor(val binding:RecyclerviewFavorlistBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHfavor {
        return VHfavor(RecyclerviewFavorlistBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VHfavor, position: Int) {
        var item = items[position]
        holder.binding.tvPlaceName.text = item.placeName
        holder.binding.tvAddress.text = item.roadAddress
        holder.binding.tvDistance.text = item.distance


        holder.binding.root.setOnClickListener {
            val intent = Intent(context, ItemDetailActivity::class.java)
            val gson= Gson()
            val s:String = gson.toJson(item) // 객체 --> json String
            intent.putExtra("favoriteItem",s)
            context.startActivity(intent)
        }
    }




}
package com.hsr2024.tpwalkthehood.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.LayoutInflaterCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.gson.Gson
import com.hsr2024.tpwalkthehood.data.KakaoSearchPlaceResponse
import com.hsr2024.tpwalkthehood.data.Place
import com.hsr2024.tpwalkthehood.databinding.RecyclerviewSearchItemBinding
import com.hsr2024.tpwalkthehood.tab1.ItemDetailActivity

// tab1 리스트 아이템 디테일 목록

class PlaceItemAdapter(val context: Context,val itemList:List<Place>): Adapter<PlaceItemAdapter.VHplace>(){

    inner class VHplace(val binding:RecyclerviewSearchItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHplace {
        return VHplace(RecyclerviewSearchItemBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: VHplace, position: Int) {
        val item:Place = itemList[position]

        holder.binding.tvPlaceName.text = item.place_name
        holder.binding.tvAddress.text = if (item.road_address_name == "") item.address_name else item.road_address_name
        holder.binding.tvPhone.text = item.phone
        holder.binding.tvDistance.text = "${item.distance}M"

        holder.binding.root.setOnClickListener {
            val intent = Intent(context,ItemDetailActivity::class.java)
            val gson= Gson()
            val s:String = gson.toJson(item) // 객체 --> json String
            intent.putExtra("place",s)
            context.startActivity(intent)
        }

        }
    }


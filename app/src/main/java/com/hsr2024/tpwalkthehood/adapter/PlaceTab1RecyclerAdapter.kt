package com.hsr2024.tpwalkthehood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.data.Place
import com.hsr2024.tpwalkthehood.databinding.RecyclerviewSearchItemBinding

class PlaceTab1RecyclerAdapter(val context:Context,val placeList:List<Place>) : Adapter<PlaceTab1RecyclerAdapter.VH>() {

    inner class VH(val binding: RecyclerviewSearchItemBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = RecyclerviewSearchItemBinding.inflate(LayoutInflater.from(context),parent,false)

        return VH(binding)
    }

    override fun getItemCount(): Int = placeList.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val place: Place = placeList[position]

        holder.binding.tvPlaceName.text = place.place_name
        holder.binding.tvAddress.text = if ( place.road_address_name == "") place.address_name else place.road_address_name
        holder.binding.tvPhone.text = place.phone
        holder.binding.tvDistance.text = "${place.distance}M"

    }


}
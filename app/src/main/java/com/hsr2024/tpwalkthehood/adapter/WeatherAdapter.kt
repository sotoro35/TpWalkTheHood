package com.hsr2024.tpwalkthehood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.data.ModelWeather
import com.hsr2024.tpwalkthehood.databinding.RecyclerViewWeatherBinding

// tab1 날씨 리사이클러뷰
class WeatherAdapter(val context: Context,var items:List<ModelWeather>):Adapter<WeatherAdapter.VHweather>() {
    inner class VHweather(val binding:RecyclerViewWeatherBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHweather {
        return VHweather(RecyclerViewWeatherBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VHweather, position: Int) {
        var item = items[position]
        holder.binding.weatherTime.text = ModelWeather.convertTime(item.fcstTime)
        holder.binding.weatherTemp.text = "${item.temp} 도"
        holder.binding.weatherIcon.setImageResource(ModelWeather.getSky(item.rainType,item.sky))
        holder.binding.weatherTv.text = ModelWeather.getSkyString(item.rainType,item.sky)
    }
}
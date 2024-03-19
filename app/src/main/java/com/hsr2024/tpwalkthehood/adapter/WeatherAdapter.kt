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

class WeatherAdapter(val context: Context,var items:Array<ModelWeather>):Adapter<WeatherAdapter.VHweather>() {
    inner class VHweather(val binding:RecyclerViewWeatherBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHweather {
        return VHweather(RecyclerViewWeatherBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VHweather, position: Int) {
        var item = items[position]
        holder.binding.weatherTime.text = item.fcstTime
        holder.binding.weatherTemp.text = item.temp
        holder.binding.weatherIcon.setImageResource(getSky(item.rainType,item.sky))

    }

    fun getSky(rainType:String, sky :String):Int{
        return if (rainType == "0"){
            when(sky){
                "1" -> R.drawable.weather_sun //맑음
                "3" -> R.drawable.weather_sun //그름 많음
                "4" -> R.drawable.weather_sun //흐림
                else -> R.drawable.weather_sun
            }
        } else {
            when(rainType){
                "1" -> R.drawable.weather_sun //비
                "2" -> R.drawable.weather_sun //비/눈
                "3" -> R.drawable.weather_sun //눈
                else -> R.drawable.weather_sun
            }
        }
    }// getSky
}
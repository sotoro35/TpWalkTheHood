package com.hsr2024.tpwalkthehood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hsr2024.tpwalkthehood.R

class MainCategoryAdapter(val context: Context, val categories: Array<String>,
                          val onItemClick: (String) -> Unit) : RecyclerView.Adapter<MainCategoryAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tv: TextView = itemView.findViewById(R.id.choice_01)

        fun bind(category: String){
            tv.text = category
            itemView.setOnClickListener {
                onItemClick(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recyclerview_category,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)
    }
}


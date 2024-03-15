package com.hsr2024.tpwalkthehood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hsr2024.tpwalkthehood.R

class SubCategoryAdapter(val context: Context,val subCategories:ArrayList<String>, val onItemClick: (String) -> Unit ): Adapter<SubCategoryAdapter.MySubViewHolder>() {

    inner class MySubViewHolder(itemview: View) : ViewHolder(itemview){
        val tv: TextView = itemview.findViewById(R.id.sub_01)

        fun bind(subCategory: String){
            tv.text = subCategory
            itemView.setOnClickListener { onItemClick(subCategory) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySubViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recyclerview_category_sub,parent,false)
        return MySubViewHolder(view)
    }

    override fun getItemCount(): Int = subCategories.size

    override fun onBindViewHolder(holder: MySubViewHolder, position: Int) {
        holder.bind(subCategories[position])
    }

}


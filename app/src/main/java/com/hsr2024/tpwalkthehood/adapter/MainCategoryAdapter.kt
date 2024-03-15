package com.hsr2024.tpwalkthehood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hsr2024.tpwalkthehood.R

class MainCategoryAdapter(val context: Context, val categories: Array<String>,
                          val onItemClick: (String) -> Unit) : RecyclerView.Adapter<MainCategoryAdapter.MyViewHolder>() {


                              // 선택된 아이템의 위치를 저장하는 변수
    private var selectedItemPosition: Int = RecyclerView.NO_POSITION

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tv: TextView = itemView.findViewById(R.id.choice_01)

        fun bind(category: String, position: Int){
            tv.text = category

            // 선택된 아이템의 경우 텍스트 색상 변경
            if (position == selectedItemPosition) {
                tv.setTextColor(ContextCompat.getColor(context, R.color.logo_background))
            } else {
                tv.setTextColor(ContextCompat.getColor(context, R.color.black))
            }
            itemView.setOnClickListener {
                onItemClick(category)
                // 선택된 아이템의 위치 저장 및 색상 변경
                selectedItemPosition = adapterPosition
                notifyDataSetChanged()
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
        holder.bind(category,position)
    }


    // 앱을 시작하자마자 클릭되도록...
    fun selectItem(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }
}


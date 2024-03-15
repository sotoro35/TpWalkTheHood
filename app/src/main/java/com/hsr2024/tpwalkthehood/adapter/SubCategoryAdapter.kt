package com.hsr2024.tpwalkthehood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hsr2024.tpwalkthehood.R

class SubCategoryAdapter(val context: Context,val subCategories:Array<String>, val subCategoryImages: Array<Int>,
                         val onItemClick: (String) -> Unit ): Adapter<SubCategoryAdapter.MySubViewHolder>() {

    // 선택된 아이템의 위치를 저장하는 변수
    private var selectedItemPosition: Int = RecyclerView.NO_POSITION


    inner class MySubViewHolder(itemview: View) : ViewHolder(itemview){
        val tv: TextView = itemview.findViewById(R.id.sub_01)
        val imageView: ImageView = itemView.findViewById(R.id.btn_companion_pets)

        fun bind(subCategory: String, imageResId: Int){
            tv.text = subCategory

            // 선택된 아이템의 경우 텍스트 색상 변경
            if (position == selectedItemPosition) {
                tv.setTextColor(ContextCompat.getColor(context, R.color.logo_background))
                imageView.setBackgroundResource(R.drawable.button_selected)
            } else {
                tv.setTextColor(ContextCompat.getColor(context, R.color.black))
                imageView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            }

            // 이미지 설정
            imageView.setImageResource(imageResId)

            itemView.setOnClickListener {
                onItemClick(subCategory)

                // 선택된 아이템의 위치 저장 및 색상 변경
                selectedItemPosition = position
                notifyDataSetChanged()}

        } // bind...


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySubViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recyclerview_category_sub,parent,false)
        return MySubViewHolder(view)
    }

    override fun getItemCount(): Int = subCategories.size

    override fun onBindViewHolder(holder: MySubViewHolder, position: Int) {
        holder.bind(subCategories[position],subCategoryImages[position])
    }

    // 대분류 클릭시 소분류 첫번째
    fun selectFirstItem() {
        selectedItemPosition = 0 // 첫 번째 항목 선택
        notifyDataSetChanged() // 어댑터에 데이터 변경 알림
    }// selectFirstItem...

}


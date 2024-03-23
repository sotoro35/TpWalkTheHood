package com.hsr2024.tpwalkthehood.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.data.CategoryItem
import com.hsr2024.tpwalkthehood.databinding.RecyclerviewCategorySubBinding

// tab1 서브카테고리 리사이클러뷰
class subCategoryTestAdapter(val context: Context, val subitems:List<CategoryItem>,val onItemClick: (CategoryItem) -> Unit) : Adapter<subCategoryTestAdapter.VHsub>(){

    inner class VHsub(val binding:RecyclerviewCategorySubBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHsub {
        val binding = RecyclerviewCategorySubBinding.inflate(LayoutInflater.from(context),parent,false)
        return VHsub(binding)
    }

    override fun getItemCount(): Int = subitems.size

    override fun onBindViewHolder(holder: VHsub, position: Int) {
        holder.binding.sub01.text = subitems[position].category
        holder.binding.btnCompanionPets.setImageResource(subitems[position].categoryIcon)

        if (selectedItemPosition == position) {
            holder.binding.root.setBackgroundResource(R.drawable.bg_subitem)
        } else {
            holder.binding.root.setBackgroundResource(R.drawable.ripple_background)
        }


        // 클릭했을때 배경색 변경, 클릭한 텍스트 콜백
        holder.binding.root.setOnClickListener {
            // 이전에 선택된 아이템의 배경색을 원래대로 되돌립니다.
            val prevSelectedItemPosition = selectedItemPosition
            selectedItemPosition = holder.adapterPosition
            if (prevSelectedItemPosition != -1) {
                notifyItemChanged(prevSelectedItemPosition)
            }
            // 현재 클릭된 아이템의 배경색을 변경합니다.
            notifyItemChanged(selectedItemPosition)

            //클릭한 아이템의 텍스트를 콜백
           onItemClick(subitems[position])

        }

    }

    // 첫 번째 아이템이 선택되도록 초기화
    private var selectedItemPosition = 0

}
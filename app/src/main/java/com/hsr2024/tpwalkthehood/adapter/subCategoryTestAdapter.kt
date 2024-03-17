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


        holder.binding.root.setOnClickListener {

            val prevSelectedItemPosition = selectedItemPosition
            selectedItemPosition = holder.adapterPosition
            if (prevSelectedItemPosition != -1) {
                notifyItemChanged(prevSelectedItemPosition)
            }
            // 현재 클릭된 아이템의 배경색을 변경합니다.
            notifyItemChanged(selectedItemPosition)

            onItemClick(subitems[position])

        }

    }

    private var selectedItemPosition = -1

}
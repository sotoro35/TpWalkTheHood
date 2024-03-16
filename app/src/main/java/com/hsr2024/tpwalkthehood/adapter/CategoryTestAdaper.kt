package com.hsr2024.tpwalkthehood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hsr2024.tpwalkthehood.data.CategoryItem
import com.hsr2024.tpwalkthehood.databinding.RecyclerviewCategorySubBinding

class CategoryTestAdaper(val context: Context, val category:List<CategoryItem>) : Adapter<CategoryTestAdaper.VHtest>() {

    inner class VHtest(val binding: RecyclerviewCategorySubBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHtest {
        val binding = RecyclerviewCategorySubBinding.inflate(LayoutInflater.from(context),parent,false)
        return VHtest(binding)
    }

    override fun getItemCount(): Int = category.size

    override fun onBindViewHolder(holder: VHtest, position: Int) {
        var categoryItem = category[position]
        holder.binding.btnCompanionPets.setImageResource(categoryItem.categoryIcon)
        holder.binding.sub01.text = categoryItem.category

    }


}
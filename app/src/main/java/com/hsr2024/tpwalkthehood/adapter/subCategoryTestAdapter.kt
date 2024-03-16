package com.hsr2024.tpwalkthehood.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
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

        holder.binding.root.setOnClickListener {
            onItemClick(subitems[position])
        }


    }
}
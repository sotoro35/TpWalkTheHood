//package com.hsr2024.tpwalkthehood.adapter
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView.Adapter
//import androidx.recyclerview.widget.RecyclerView.ViewHolder
//import com.hsr2024.tpwalkthehood.data.CategoryItem
//import com.hsr2024.tpwalkthehood.databinding.RecyclerviewCategorySubBinding
//
//class CategoryAdapter(val context: Context, val categoryList: List<CategoryItem>) : Adapter<CategoryAdapter.VHcategory>(){
//    inner class VHcategory(val binding:RecyclerviewCategorySubBinding) : ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHcategory {
//        val binding = RecyclerviewCategorySubBinding.inflate(LayoutInflater.from(context),parent,false)
//        return VHcategory(binding)
//    }
//
//    override fun getItemCount(): Int = categoryList.size
//
//    override fun onBindViewHolder(holder: VHcategory, position: Int) {
//        holder.binding.subTv.text = categoryList[position].category
//        holder.binding.subIv.setImageResource( categoryList[position].categoryIcon)
//
//    }
//}
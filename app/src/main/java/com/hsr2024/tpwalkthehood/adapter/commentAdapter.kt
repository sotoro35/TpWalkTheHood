package com.hsr2024.tpwalkthehood.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hsr2024.tpwalkthehood.FeedString
import com.hsr2024.tpwalkthehood.G
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.data.CommentItem
import com.hsr2024.tpwalkthehood.databinding.RecyclerviewCommentBinding
import java.text.SimpleDateFormat

class commentAdapter(val context: Context, var list: List<CommentItem>):Adapter<commentAdapter.VHcomment>() {

    inner class VHcomment(val binding:RecyclerviewCommentBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHcomment {
        return VHcomment(RecyclerviewCommentBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: VHcomment, position: Int) {
        var item = list[position]
        var s:String = date("${item.date}")
        var imgUrl= "http://ruaris.dothome.co.kr/WalkTheHood/${item.profile}"


        holder.binding.commentNickname.text = item.nickname
        holder.binding.commentText.text = item.text
        holder.binding.commentDate.text = s


        if (item.profile == "1" || item.profile == null || item.profile == ""){
            holder.binding.commentProfile.setImageResource(R.drawable.profile)
        }else Glide.with(context).load(imgUrl).into(holder.binding.commentProfile)


        if (item.email.equals("${G.userAccount?.email}")){
            holder.binding.commentDelete.visibility = View.VISIBLE

            holder.binding.commentDelete.setOnClickListener {

                val builder = AlertDialog.Builder(context)
                builder.setTitle("정말 삭제하시겠습니까?")
                builder.setPositiveButton("확인"){ dialog, which ->
                    val db = Firebase.firestore
                    val dbCom = db.collection("Posts").document("${FeedString.documentId}").collection("comments")
                    dbCom.whereEqualTo("comment_email","${G.userAccount?.email}")
                        .whereEqualTo("date",item.date)
                        .get()
                        .addOnSuccessListener {
                            for (document in it.documents){
                                dbCom.document(document.id)
                                    .delete()
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "삭제완료", Toast.LENGTH_SHORT).show() }

                                // 삭제된 댓글을 데이터 리스트에서 제거하고 RecyclerView를 다시 그립니다.
                                list = list.filter { it.date != item.date }
                                notifyDataSetChanged()
                            }
                        }
                }
                builder.setNegativeButton("취소"){ dialog, which ->
                    dialog.dismiss()
                }

                builder.create().show()

            }
        }

    }

    fun date(dateString:String):String{
        var inputFormat = SimpleDateFormat("yyyyMMddHHmmss")
        var outputFormat = SimpleDateFormat("yyyy년MM월dd월HH:mm")

        var date = inputFormat.parse(dateString)
        var formatDate = outputFormat.format(date).toString()

        return formatDate
    }

}
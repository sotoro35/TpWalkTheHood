package com.hsr2024.tpwalkthehood.tab3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.hsr2024.tpwalkthehood.FeedString
import com.hsr2024.tpwalkthehood.G
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.databinding.ActivityFeedDetailBinding
import com.hsr2024.tpwalkthehood.tab5.EditMyFeed

// Feed에서 아이템 클릭시 보여주는 상세내역
// 서버에서 내용 가져와야함
// 좋아요 / 댓글 / 친구신청 클릭시 서버로 정보 전달

class FeedDetailActivity : AppCompatActivity() {

    val binding by lazy {ActivityFeedDetailBinding.inflate(layoutInflater) }

    var imgUrl= "http://ruaris.dothome.co.kr/WalkTheHood/${FeedString.profile}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.title.setNavigationOnClickListener { finish() }

        binding.nickname.text = FeedString.nickname
        binding.tvFeedTitle.text = FeedString.title
        binding.tvFeedText.text =FeedString.text
        binding.feedIv.visibility = View.VISIBLE
        binding.btnEditF.visibility = View.INVISIBLE
        binding.btnDeleteF.visibility = View.INVISIBLE



        binding.btnEditF.setOnClickListener {
            startActivity(Intent((this),EditMyFeed::class.java))
            finish()}
        binding.btnDeleteF.setOnClickListener { feedDelete() }

        if(FeedString.profile == null || FeedString.profile == "1"){
            binding.ivProfile.setImageResource(R.drawable.profile)
        }else Glide.with(this).load(imgUrl).into(binding.ivProfile)

        if (FeedString.downUrl == null || FeedString.downUrl == "1"){
            binding.feedIv.visibility = View.GONE
        } else {
            binding.feedIv.visibility = View.VISIBLE
            Glide.with(this).load(FeedString.downUrl).into(binding.feedIv)
        }


        if (FeedString.email == G.userAccount?.email){
            binding.btnEditF.visibility = View.VISIBLE
            binding.btnDeleteF.visibility = View.VISIBLE
        }

    }//onCreate...

    private fun feedDelete(){
        val feedDeRef = Firebase.firestore.collection("Posts")
            feedDeRef.whereEqualTo("email",G.userAccount?.email)
                .whereEqualTo("date",FeedString.date)
                .whereEqualTo("nickname",FeedString.nickname)
                .get().addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        feedDeRef.document(document.id)
                            .delete()
                            .addOnSuccessListener {

                                //파일 삭제시 storage 에서도 사진 삭제....
                                if (FeedString.fileName != null && FeedString.fileName != "1") {
                                    val feedDimgRef =
                                        Firebase.storage.getReference("FeedImage/${FeedString.fileName}")
                                    feedDimgRef.delete().addOnSuccessListener {
                                        Toast.makeText(this, "삭제완료", Toast.LENGTH_SHORT).show()
                                    }
                                        .addOnFailureListener { Log.e("오류", "삭제오류") }
                                }

                                AlertDialog.Builder(this).setMessage("${FeedString.fileName}").create().show()
                               finish()
                            }
                    }//for

                }//addOnSuccessListener
    }
}
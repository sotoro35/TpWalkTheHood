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
import com.hsr2024.tpwalkthehood.adapter.commentAdapter
import com.hsr2024.tpwalkthehood.data.CommentItem
import com.hsr2024.tpwalkthehood.databinding.ActivityFeedDetailBinding
import com.hsr2024.tpwalkthehood.tab5.EditMyFeed

// Feed에서 아이템 클릭시 보여주는 상세내역
// 서버에서 내용 가져와야함
// 좋아요 / 댓글 / 친구신청 클릭시 서버로 정보 전달

class FeedDetailActivity : AppCompatActivity() {

    val binding by lazy {ActivityFeedDetailBinding.inflate(layoutInflater) }

    var imgUrl= "http://ruaris.dothome.co.kr/WalkTheHood/${FeedString.profile}"

    private var isLike= false
    var likeNumber = FeedString.likeNum.toInt()


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

        binding.favornum.text = FeedString.likeNum
        binding.favorFeed.isEnabled = true




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

        loadLike()

        binding.favorFeed.setOnClickListener {

            val db = Firebase.firestore.collection("Posts")
            val dbLike = db.document("${FeedString.documentId}").collection("Like")

            val likeId = mutableMapOf<String,Any>(
                "email" to "${G.userAccount?.email}"
            )

            if (isLike){
                binding.favorFeed.isEnabled = false
                // 좋아요 취소
                dbLike.whereEqualTo("email","${G.userAccount?.email}")
                    .get().addOnSuccessListener {
                        for (document in it.documents){
                            dbLike.document(document.id)
                                .delete()
                                .addOnSuccessListener {
                                    Toast.makeText(this, "좋아요 취소", Toast.LENGTH_SHORT).show()

                                    val db = Firebase.firestore
                                    val likeNumDocRef = db.collection("Posts").document("${FeedString.documentId}")
                                    db.runTransaction {transaction ->
                                        var likeNum = transaction.get(likeNumDocRef).getLong("likeNum") ?: 0
                                        transaction.update(likeNumDocRef,"likeNum",likeNum -1)
                                    }
                                        .addOnSuccessListener {
                                            // 좋아요 수 업데이트
                                            val updateNumRef = db.collection("Posts").document("${FeedString.documentId}")
                                            updateNumRef.get().addOnSuccessListener {
                                                var updateNum = it.getLong("likeNum")?:0
                                                binding.favornum.text = updateNum.toString()
                                                binding.favorFeed.isEnabled = true
                                            }
                                        }
                                    }
                                }
                        }

              // binding.favorFeed.isEnabled = true
            }else{
                binding.favorFeed.isEnabled = false
                //좋아요 추가
                dbLike.add(likeId)
                    .addOnSuccessListener {
                        Toast.makeText(this, "좋아요", Toast.LENGTH_SHORT).show()

                        val db = Firebase.firestore
                        val likeNumDocRef = db.collection("Posts").document("${FeedString.documentId}")
                        db.runTransaction {transaction ->
                            var likeNum = transaction.get(likeNumDocRef).getLong("likeNum") ?: 0
                            transaction.update(likeNumDocRef,"likeNum",likeNum +1)
                        }.addOnSuccessListener {
                            // 좋아요 수 업데이트
                            val updateNumRef = db.collection("Posts").document("${FeedString.documentId}")
                            updateNumRef.get().addOnSuccessListener {
                                var updateNum = it.getLong("likeNum")?:0
                                binding.favornum.text = updateNum.toString()
                                binding.favorFeed.isEnabled = true
                            }
                        }
                    }
                //binding.favorFeed.isEnabled = true
            }


            isLike =! isLike
            if (isLike) {
                binding.favorFeed.setImageResource(R.drawable.heart_select)
                binding.favornum.text = (likeNumber+1).toString() // 좋아요 수 업데이트
            } else {
                binding.favorFeed.setImageResource(R.drawable.heart)
                if (likeNumber > 0 ){
                    binding.favornum.text = (likeNumber-1).toString() // 좋아요 수 업데이트
                }
            }
        }//setOnClickListener



    }//onCreate...


    override fun onResume() {
        super.onResume()

        loadComment()

    }

    private fun saveComment(){

    }

    private fun loadComment(){
        val db = Firebase.firestore.collection("Posts")
        db.document("${FeedString.documentId}").collection("comments")
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty){
                    val commentsList = mutableListOf<CommentItem>()
                    var item:CommentItem

                    for (document in it.documents){
                        var email = document.getString("comment_email")
                        var nickname = document.getString("comment_nickname")
                        var profile = document.getString("comment_profile")
                        var text = document.getString("text")
                        var date = document.getString("date")

                        item = CommentItem(email!!,nickname!!,profile,text!!,date!!)
                        commentsList.add(0,item)

                    }
                    val adapter = commentAdapter(this,commentsList)
                    binding.recyclerComment.adapter = adapter
                    adapter.notifyDataSetChanged()
                    AlertDialog.Builder(this).setMessage("${commentsList.get(0).nickname}").create().show()
                    Log.e("오류아님","댓글가져오기 성공")
                }



            }.addOnFailureListener {
                Log.e("오류댓글","댓글가져오기 오류: ${it.message}") }
    }

    private fun loadLike(){
        val doc = Firebase.firestore.collection("Posts")
            doc.document("${FeedString.documentId}")
            .collection("Like")
            .whereEqualTo("email","${G.userAccount?.email}")
            .get().addOnSuccessListener {
                isLike = !it.isEmpty // true 선택됨 / false 선택안됨

                if (isLike) binding.favorFeed.setImageResource(R.drawable.heart_select)
                else binding.favorFeed.setBackgroundResource(R.drawable.heart)
            }
    }


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
                                        //Toast.makeText(this, "삭제완료", Toast.LENGTH_SHORT).show()
                                    }
                                        .addOnFailureListener { Log.e("오류", "삭제오류") }
                                }

                                //AlertDialog.Builder(this).setMessage("${FeedString.fileName}").create().show()
                               finish()
                            }
                    }//for

                }//addOnSuccessListener
    }
}
package com.hsr2024.tpwalkthehood.tab5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hsr2024.tpwalkthehood.G
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.adapter.MyFeedAdapter
import com.hsr2024.tpwalkthehood.adapter.Tab3FeedAdapter
import com.hsr2024.tpwalkthehood.data.FeedItem
import com.hsr2024.tpwalkthehood.databinding.ActivityMyFeedBinding

// 3번째이동.. 내 게시물보기모드.. Feed의 내용물과 같게 보일거지만 수정삭제버튼이 있음..
// 수정누르면 수정화면으로 이동, 삭제누르면 서버에서 삭제
class MyFeedActivity : AppCompatActivity() {

    val binding by lazy { ActivityMyFeedBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setOnClickListener { finish() }

        loadMyFeed()

    }

    override fun onResume() {
        super.onResume()
        loadMyFeed()
    }

    private fun loadMyFeed(){
        val myFeedRef = Firebase.firestore.collection("Posts")

        myFeedRef.whereEqualTo("email", G.userAccount?.email)
            .get()
            .addOnSuccessListener {querySnapshot ->
                val postList = mutableListOf<FeedItem>()
                var postitem: FeedItem


                for (document in querySnapshot.documents){
                    var email: String = document.getString("email")!!
                    var nickname: String? = document.getString("nickname")?: "닉넴없음"
                    var title:String = document.getString("title")!!
                    var text:String = document.getString("text")!!
                    var date:String = document.getString("date")!!
                    var downUrl:String = document.getString("downUrl")?: "1"
                    var profile:String = document.getString("profile")?: "1"
                    var fileName:String = document.getString("fileName") ?:"1"
                    var documentId = document.id
                    var like:String = document.getString("like")?: "1"
                    var likeNum:Long = document.getLong("likeNum")?: 0L


                    postitem = FeedItem(email, nickname, title, text, date, downUrl, profile, fileName,documentId,like,likeNum)
                    postList.add(0,postitem)
                }

                val adapter = MyFeedAdapter(this,postList)
                binding.recyclerViewMyFeed.adapter = adapter
                adapter.notifyDataSetChanged()
            }
    }//loadMyFeed

}
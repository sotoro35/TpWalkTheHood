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

        //testload()
        loadMyFeed()

    }

    override fun onResume() {
        super.onResume()
        loadMyFeed()
    }

    private fun loadMyFeed(){
        val myFeedRef = Firebase.firestore.collection("Posts")
        myFeedRef
            .whereEqualTo("email", G.userAccount?.email)
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

                    postitem = FeedItem(email, nickname, title, text, date, downUrl, profile)
                    postList.add(0,postitem)
                }

                val adapter = MyFeedAdapter(this,postList)
                binding.recyclerViewMyFeed.adapter = adapter
                adapter.notifyDataSetChanged()

            }
    }//loadMyFeed

    fun testload(){
        val userEmail = G.userAccount?.email
        if (userEmail != null) {
            val myFeedRef = Firebase.firestore.collection("Posts")
            myFeedRef
                .whereEqualTo("email", userEmail)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val postList = mutableListOf<FeedItem>()
                    for (document in querySnapshot.documents) {
                        val email: String = document.getString("email")!!
                        val nickname: String? = document.getString("nickname") ?: "닉넴없음"
                        val title: String = document.getString("title")!!
                        val text: String = document.getString("text")!!
                        val date: String = document.getString("date")!!
                        val downUrl: String = document.getString("downUrl") ?: "1"
                        val profile: String = document.getString("profile") ?: "1"

                        val postitem = FeedItem(email, nickname, title, text, date, downUrl, profile)
                        postList.add(0, postitem)
                    }

                    val adapter = MyFeedAdapter(this, postList)
                    binding.recyclerViewMyFeed.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    // 데이터 가져오기 실패 시 처리
                    Log.w("오류Feed", "Error getting documents: ", exception)
                }
        } else {
            AlertDialog.Builder(this).setMessage("이메일null").create().show()
            Log.w("오류Feed", "사용자 이메일이 null입니다.")
        }
    }









    fun clickFirebase(){

//        val test: CollectionReference = Firebase.firestore.collection("emailUsers")
//        test.firestore.document("sotoro11@naver.com").collection("FavoriteList").document("favorite001").get().addOnCompleteListener {
//
//            var s= it?.result?.data?.get("favorUrl").toString()
//            AlertDialog.Builder(this).setMessage("$s").create().show()
//        }
//
//        var db:CollectionReference = Firebase.firestore.collection("emailUsers")
//            db.document("sotoro11@naver.com").collection("FavoriteList").get().addOnSuccessListener {
//                result->
//                for (document in result){
//                    Log.d("db성공","${document.id} => ${document.data}")
//                    AlertDialog.Builder(this).setMessage("${document.id} => ${document.data}").create().show()
//                }
//            }
//                .addOnFailureListener { exception->
//                    Log.d("db실패", "Error getting documents: ", exception)
//                }


    }
}
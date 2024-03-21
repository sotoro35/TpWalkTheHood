package com.hsr2024.tpwalkthehood.tab1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.hsr2024.tpwalkthehood.G
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.data.Place
import com.hsr2024.tpwalkthehood.databinding.ActivityItemDetailBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//아이템 클릭시 웹뷰로 보여주기.. 서버작업... 뒤로가기버튼.. 찜버튼
class ItemDetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityItemDetailBinding.inflate(layoutInflater) }
    private lateinit var place:Place

    private var isFavorite= false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val s: String? = intent.getStringExtra("place")
        s?.also {//also는 전체를 리턴

            place = Gson().fromJson(it, Place::class.java) // json --> 객체로

            // 웹뷰 필수 3가지 설정
            binding.webView.webViewClient = WebViewClient() // 내가 만든 뷰 안에 들어가도록 설정
            binding.webView.webChromeClient = WebChromeClient() // 웹 문서안에 다이얼로그 같은 기능이 뜰 수 있도록 설정
            binding.webView.settings.javaScriptEnabled = true // 자바스크립트 허용

            binding.webView.loadUrl(place.place_url)

        }//also..

        loadFavorite()


        //postDto.favorite[uid!!] = true
        binding.favor.setOnClickListener {

            val data = place.place_url
            val favorlist: MutableMap<String, Any> = mutableMapOf(
            "place_name" to "${place.place_name}",
            "road_address_name" to "${place.road_address_name}",
            "place.distance" to "${place.distance}",
            "email" to "${G.userAccount!!.email}",
            "url" to "${data}"
            )
//            favorlist["place_name"] = place.place_name
//            favorlist["road_address_name"] = place.road_address_name
//            favorlist["place.distance"] = place.distance
//            favorlist["email"] = G.userAccount!!.email
//            favorlist["url"] = data


            val favorRef = Firebase.firestore.collection("emailUsers")

            if (!isFavorite) {
                favorRef.document("${G.userAccount!!.email}")
                    .update("aaa",favorlist)
                    .addOnSuccessListener {
                        Toast.makeText(this, "찜 추가", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener { e -> Log.w("찜", "추가에러", e) }


            } else {
                favorRef.document("${G.userAccount!!.email}")
                    .update("favorite", FieldValue.arrayRemove("${data}"))
                    .addOnSuccessListener {
                        Toast.makeText(this, "찜 삭제", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e -> Log.w("찜", "삭제에러", e) }
            }

//            if (!isFavorite) {
//                favorRef.document("${G.userAccount!!.email}")
//                .update("${data}", FieldValue.arrayUnion("${data}"))
//                .addOnSuccessListener {
//                    Toast.makeText(this, "찜 추가", Toast.LENGTH_SHORT).show()
//                }.addOnFailureListener { e -> Log.w("찜", "추가에러", e) }
//
//
//            } else {
//                favorRef.document("${G.userAccount!!.email}")
//                    .update("favorite", FieldValue.arrayRemove("${data}"))
//                    .addOnSuccessListener {
//                        Toast.makeText(this, "찜 삭제", Toast.LENGTH_SHORT).show()
//                    }
//                    .addOnFailureListener { e -> Log.w("찜", "삭제에러", e) }
//            }

            isFavorite =! isFavorite // 불린값을 반대로 바꿀때.. 현재 false라면 true라고 바꾸는것
            if (isFavorite) binding.favor.setImageResource(R.drawable.heart_select)
            else binding.favor.setImageResource(R.drawable.heart)

        }
    }//onCreate...




    private fun loadFavorite() {

        // 서버에 저장이 되어있는지 확인
        val data = place.place_url
        val favorRef = Firebase.firestore.collection("emailUsers")
            favorRef.document("${G.userAccount!!.email}")
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()){
                        val favoriteArray = documentSnapshot.get("favorite") as? ArrayList<String>
                        if (favoriteArray != null && data in favoriteArray){
                            isFavorite = true
                            binding.favor.setImageResource(R.drawable.heart_select)

                        }else{
                            isFavorite = false
                            binding.favor.setImageResource(R.drawable.heart)
                        }

                    }else Log.e("에러","문서를 찾지 못했습니다")

                }
                .addOnFailureListener { Log.e("에러","loadFavorite 실패") }

    }


    // 뒤로가기 버튼 눌렀을때 1칸만 뒤로 가도록...

    override fun onBackPressed(){
        if (binding.webView.canGoBack()) binding.webView.goBack() // 뒤로가는 페이지가 있다면 그 페이지로 가
        else super.onBackPressed() // 걍 꺼버려
    }
}
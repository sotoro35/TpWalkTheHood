package com.hsr2024.tpwalkthehood.tab5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.databinding.ActivityMyFeedBinding

// 3번째이동.. 내 게시물보기모드.. Feed의 내용물과 같게 보일거지만 수정삭제버튼이 있음..
// 수정누르면 수정화면으로 이동, 삭제누르면 서버에서 삭제
class MyFeedActivity : AppCompatActivity() {

    val binding by lazy { ActivityMyFeedBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setOnClickListener { clickFirebase() }
    }

    fun clickFirebase(){

//        val test: CollectionReference = Firebase.firestore.collection("emailUsers")
//        test.firestore.document("sotoro11@naver.com").collection("FavoriteList").document("favorite001").get().addOnCompleteListener {
//
//            var s= it?.result?.data?.get("favorUrl").toString()
//            AlertDialog.Builder(this).setMessage("$s").create().show()
//        }

        var db:CollectionReference = Firebase.firestore.collection("emailUsers")
            db.document("sotoro11@naver.com").collection("FavoriteList").get().addOnSuccessListener {
                result->
                for (document in result){
                    Log.d("db성공","${document.id} => ${document.data}")
                    AlertDialog.Builder(this).setMessage("${document.id} => ${document.data}").create().show()
                }
            }
                .addOnFailureListener { exception->
                    Log.d("db실패", "Error getting documents: ", exception)
                }


    }
}
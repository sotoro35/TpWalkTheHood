package com.hsr2024.tpwalkthehood.tab5

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.hsr2024.tpwalkthehood.FeedString
import com.hsr2024.tpwalkthehood.G
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.adapter.Tab3FeedAdapter
import com.hsr2024.tpwalkthehood.data.FeedItem
import com.hsr2024.tpwalkthehood.databinding.ActivityEditMyFeedBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditMyFeed : AppCompatActivity() {

    val bindig by lazy { ActivityEditMyFeedBinding.inflate(layoutInflater) }
    var imgUri:Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindig.root)

        bindig.toolbar.setNavigationOnClickListener { finish() }

        bindig.editTitle.setText("${FeedString.title}")
        bindig.editText.setText("${FeedString.text}")
        Glide.with(this).load("${FeedString.downUrl}").into(bindig.ivImage)

        bindig.btnImage.setOnClickListener { clickImage() }
        bindig.myFeedSave.setOnClickListener { saveFeed() }

        bindig.myFeedSave.isEnabled = true


    }//onCreate..

    private fun clickImage(){
        val intent = if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU) Intent(MediaStore.ACTION_PICK_IMAGES)
        else Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*")
        resultLauncher.launch(intent)
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        imgUri = it.data?.data
        if (imgUri != null) Glide.with(this).load(imgUri).into(bindig.ivImage)
        else Glide.with(this).load("${FeedString.downUrl}").into(bindig.ivImage)
    }



// 수정버튼을 누르고 온 상태... 이미 해당 값이 FeedString 전역변수에 들어있음
    private fun saveFeed(){

        var title = bindig.inputEditTitle.editText!!.text.toString()
        var text = bindig.inputEditText.editText!!.text.toString()

        // 내 정보를 찾아와서 업데이트
        if (title.isNotEmpty() && text.isNotEmpty()){
            Glide.with(this).load(R.drawable.loading).into(bindig.loading)
            bindig.myFeedSave.isEnabled = false

            val editMyFeedRef = Firebase.firestore.collection("Posts")

            if (imgUri !== null){
                val fileName = "IMG_" + SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(Date()).toString()
                val imgRef = Firebase.storage.getReference("FeedImage/$fileName")

                imgRef.putFile(imgUri!!).addOnSuccessListener {
                    imgRef.downloadUrl.addOnSuccessListener {
                        var downUrl = it.toString()

                        editMyFeedRef.whereEqualTo("email", G.userAccount?.email)
                            .whereEqualTo("date",FeedString.date)
                            .get()
                            .addOnSuccessListener {

                                for (document in it.documents){
                                    editMyFeedRef.document(document.id)
                                        .update(
                                            mapOf(
                                                "title" to title,
                                                "text" to text,
                                                "downUrl" to downUrl,
                                                "fileName" to fileName
                                            )
                                        )
                                        .addOnSuccessListener {
                                            if (FeedString.fileName != null && FeedString.fileName != "1") {
                                                val feedDimgRef =
                                                    Firebase.storage.getReference("FeedImage/${FeedString.fileName}")
                                                feedDimgRef.delete().addOnSuccessListener {
                                                    //Toast.makeText(this, "삭제완료", Toast.LENGTH_SHORT).show()
                                                }
                                                    .addOnFailureListener { Log.e("오류", "삭제오류") }
                                            }//if...

                                            AlertDialog.Builder(this).setMessage("수정완료").create().show()
                                            Handler(Looper.getMainLooper()).postDelayed({
                                                finish()},1500)
                                        }
                                }

                            }//addOnsucces
                    }
                }

                // 이미지가 없으면.... 내용만 수정
            }else{
                editMyFeedRef.whereEqualTo("email", G.userAccount?.email)
                    .whereEqualTo("date",FeedString.date)
                    .get()
                    .addOnSuccessListener {

                        for (document in it.documents){
                            editMyFeedRef.document(document.id)
                                .update(
                                    mapOf(
                                        "title" to title,
                                        "text" to text
                                    )
                                )
                                .addOnSuccessListener {
                                    AlertDialog.Builder(this).setMessage("수정완료").create().show()

                                    Handler(Looper.getMainLooper()).postDelayed({
                                        finish()},1500)
                                }
                        }
                    }//addOnsucces
            }// else

        }//if
    }//saveFeed

}// main...
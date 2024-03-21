package com.hsr2024.tpwalkthehood.tab3

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.hsr2024.tpwalkthehood.G
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.databinding.ActivityEditBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class EditActivity : AppCompatActivity() {
    val binding by lazy { ActivityEditBinding.inflate(layoutInflater) }

    private var imgUri: Uri?= null //선택된 이미지의 콘텐츠 주소(경로)
    private var downloadUrl:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //구현목록
        //myeditTitle,myedittext의 내용과 이미지 서버에 저장

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.myeditSave.setOnClickListener { saveFeed() } // 서버에 저장
        binding.tvMyeditIvselect.setOnClickListener { clickImage() }

    }//onCreate...

    private fun clickImage(){
        val intent = if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU) Intent(MediaStore.ACTION_PICK_IMAGES)
        else Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*")
        resultLauncher.launch(intent)
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        imgUri= it.data?.data
        Glide.with(this).load(imgUri).into(binding.myeditIv)
    }

    private fun saveFeed() {
        var title = binding.myeditTitle.editText!!.text.toString()
        var text = binding.myeditText.editText!!.text.toString()
        binding.myeditSave.isEnabled = false

        if (title.isNotEmpty() && text.isNotEmpty()) {
            binding.editProgress.visibility = View.VISIBLE

            // 저장소의 이미지 업로드 참조객체 얻기
            val fileName = "IMG_" + SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(Date())
            val imgRef: StorageReference = Firebase.storage.getReference("FeedImage/$fileName")

            // 이미지가 있다면 저장소에 저장
            if (imgUri !== null) {
                imgRef.putFile(imgUri!!).addOnSuccessListener {
                    downloadUrl = it.toString()
                    // 글 내용을 저장
                    val postRef = Firebase.firestore.collection("Posts")
                    var data: MutableMap<String, Any> = mutableMapOf()
                    data["profile"] = G.userAccount?.imgfile ?: R.drawable.profile
                    data["downloadUrl"] = downloadUrl?:""
                    data["email"] = G.userAccount!!.email
                    data["nickname"] = G.userAccount?.nickname ?: ""
                    data["title"] = title
                    data["text"] = text
                    data["date"] = SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(Date()).toString()

                    postRef.document().set(data)
                    binding.editProgress.visibility = View.GONE
                    binding.myeditSave.isEnabled = true
                    finish()
                }
            }else {
                // 글 내용을 저장
                val postRef = Firebase.firestore.collection("Posts")
                var data: MutableMap<String, Any> = mutableMapOf()
                data["profile"] = G.userAccount?.imgfile ?: R.drawable.profile
                data["email"] = G.userAccount!!.email
                data["nickname"] = G.userAccount?.nickname ?: ""
                data["title"] = title
                data["text"] = text
                data["date"] = SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(Date()).toString()

                postRef.document().set(data)
                binding.editProgress.visibility = View.GONE
                binding.myeditSave.isEnabled = true
                finish()
            }
        }else {
            AlertDialog.Builder(this).setMessage("모두 입력해주세요").create().show()
            binding.myeditSave.isEnabled = true
        }

    }

    //
}//main...
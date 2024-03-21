package com.hsr2024.tpwalkthehood.tab5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hsr2024.tpwalkthehood.G
import com.hsr2024.tpwalkthehood.MainActivity
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.databinding.ActivityContactusBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//5번째이동.. 문의하기.. 사진등록, 저장하기 > 내 메일로 전송
class ContactusActivity : AppCompatActivity() {

    val binding by lazy { ActivityContactusBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnSend.setOnClickListener { clickSend() }
    }

    private fun clickSend(){

        var email = binding.inputContactusEmail.editText!!.text.toString()
        var title = binding.inputContactusTitle.editText!!.text.toString()
        var text = binding.inputContactusText.editText!!.text.toString()

        if (email.isNotEmpty() && title.isNotEmpty() && text.isNotEmpty()){

        val data: MutableMap<String,String> = mutableMapOf()
        data["email"] = email
        data["text"] = text
        data["title"] = title
        data["useremail"] = G.userAccount?.email?: ""

        val contactusRef = Firebase.firestore.collection("Contactus")

        val n= SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA).format(Date())
        contactusRef.document(n).set(data).addOnSuccessListener {

            AlertDialog.Builder(this).setMessage("전송완료").create().show()

            Handler(Looper.getMainLooper()).postDelayed({
                finish() },2000)
             }

        } else AlertDialog.Builder(this).setMessage("모두 입력해주세요").create().show()

    }

}
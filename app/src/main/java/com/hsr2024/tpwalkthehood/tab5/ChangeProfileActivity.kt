package com.hsr2024.tpwalkthehood.tab5

import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.hsr2024.tpwalkthehood.G
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.data.UserAccount
import com.hsr2024.tpwalkthehood.data.UserLoginResponse
import com.hsr2024.tpwalkthehood.databinding.ActivityChangeProfileBinding
import com.hsr2024.tpwalkthehood.network.RetrofitHelper
import com.hsr2024.tpwalkthehood.network.RetrofitService
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

// 1번째이동.. 내 프로필 수정버전
// 불러오기는 비밀번호를 뺀 나머지를 다 불러옴. 연결된 계정을 보여줌. 이메일주소 or 카카오 or 구글 or 네이버
// 서버에서 내 프로필 내용 가져오기, 저장을 누르면 수정된 내역 서버로 전송
// 프로필사진,닉네임,비밀번호,동네 수정가능.
class ChangeProfileActivity : AppCompatActivity() {

    private val binding by lazy { ActivityChangeProfileBinding.inflate(layoutInflater) }

    val imgUrl= "http://ruaris.dothome.co.kr/WalkTheHood/${G.userAccount?.imgfile}"

    // 이미지의 절대경로를 저장할 멤버변수
    var imgPath:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //텍스트에 밑줄
        binding.btnChangeImage.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        loadProfile()

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnChangeProfileSave.setOnClickListener { clickChange() }
        binding.btnChangeImage.setOnClickListener { clickImage() }


    }//onCreate

    private fun clickImage(){
        // 앱에서 사진 가져오기
        val intent = if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.TIRAMISU) Intent(MediaStore.ACTION_PICK_IMAGES)
        else Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*")
        resultLauncher.launch(intent)
    }

    // 사진 가져올 대행사..
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val uri: Uri? = it.data?.data
        uri?.let {
            Glide.with(this).load(it).into(binding.ivProfile)

            // uri --> 절대경로
            imgPath= getRealPathFromUri(uri)
        }
    }//resultLauncher

    // Uri를 전달받아 실제 파일 경로를 리턴해주는 기능 메소드 구현하기
    private fun getRealPathFromUri(uri:Uri) : String? {

        // android 10 버전 부터는 Uri를 통해 파일의 실제 경로를 얻을 수 있는 방법이 없어졌음
        // 그래서 uri에 해당하는 파일을 복사하여 임시로 파일을 만들고 그 파일의 경로를 이용하여 서버에 전송

        // Uri[미디어저장소의 DB 주소]파일의 이름을 얻어오기 - DB SELECT 쿼리작업을 해주는 기능을 가진 객체를 이용
        val cursorLoader: CursorLoader = CursorLoader(this,uri, null,null,null,null)
        val cursor: Cursor? = cursorLoader.loadInBackground()
        val fileName:String? = cursor?.run {
            moveToFirst()
            getString( getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
        } // -------------------------------------------------------------------

        // 복사본이 저장될 파일의 경로와 파일명.확장자
        val file: File = File(externalCacheDir,fileName)

        // 이제 진짜 복사 작업 수행
        val inputStream: InputStream = contentResolver.openInputStream(uri) ?: return null
        val outputStream: OutputStream = FileOutputStream(file)

        // 파일복사
        while (true){
            val buf: ByteArray = ByteArray(1024) // 빈 바이트 배열[길이:1KB]
            val len:Int= inputStream.read(buf) // 스트림을 통해 읽어들인 바이트들을 buf 배열에 넣어줌 -- 읽어드린 바이트 수를 리턴해 줌
            if (len <= 0 ) break
            outputStream.write(buf, 0, len) // 덮어쓰기가 아님..
            // offset(오프셋-편차) 0을주면 0번부터 1024가 아님.. 0~1023 번 다음은 편차를 주지말고 1024 ~ 로 주라는 의미임
            // 1024길이만큼 가져오는데.. 편차없이 1024 길이만큼 받다가 읽어드린 바이트(len)의 값만큼 쓰라는 의미임..

        }// while

        // 반복문이 끝났으면 복사가 완료된 것임

        inputStream.close()
        outputStream.close()

        //AlertDialog.Builder(this).setMessage(file.absolutePath).create().show()
        return file.absolutePath
    }////////////////////////////////////////////////////////////////////////////

    //val fragment = supportFragmentManager.findFragmentById(R.id.container_fragment) as Tab5MyFragment
    private fun clickChange(){
        // 기존 내 이메일과 비교하여 프로필 저장
        var nickname = binding.inputNickname.editText!!.text.toString()
        var password = binding.inputPasswordConfirm.editText!!.text.toString()
        var passwordConfirm = binding.inputPasswordConfirm.editText!!.text.toString()

        if (password != passwordConfirm){
            AlertDialog.Builder(this).setMessage("패스워드가 다릅니다.다시 확인해주세요").create().show()
        }else{

            // 먼저 String 데이터들은 Map collection 으로 묶어서 전송 : @PartMap
            val dataPart: MutableMap<String,String> = mutableMapOf()
            dataPart["email"] = G.userAccount!!.email
            dataPart["nickname"] = nickname
            dataPart["password"] = password

            //이미지파일을 MutipartBody.Part 로 포장하여 전송: @Part
            val filePart: MultipartBody.Part? = imgPath?.let { //널이 아니면...
                val file= File(it) // 생선손질..
                val requestBody:RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(),file) // 진공팩포장
                MultipartBody.Part.createFormData("img1", file.name,requestBody) // 택배상자 포장.. == 리턴되는 값
            }

            val retrofit = RetrofitHelper.getRetrofitInstance("http://ruaris.dothome.co.kr")
            val retrofitService = retrofit.create(RetrofitService::class.java)
            retrofitService.userChangeProfile(dataPart,filePart).enqueue(object : Callback<UserLoginResponse>{
                override fun onResponse(call: Call<UserLoginResponse>, response: Response<UserLoginResponse>) {
                    val userResponse = response.body()
                    userResponse?.user?.apply {
                            G.userAccount?.nickname = userResponse.user.nickname
                            G.userAccount?.password = userResponse.user.password
                            G.userAccount?.imgfile = userResponse.user.imgfile



                        }

                        //fragment.reloadMypage() 결과를 받아와서 프래그먼트에 어떻게 구현하나... 메소드가 안먹힘
                        saveSharedPreferences()
                        finish()


                    }

                    override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                        Toast.makeText(this@ChangeProfileActivity, "서버오류", Toast.LENGTH_SHORT)
                            .show()
                        Log.e("오류", "${t.message}")
                    }
            })
        }
    }

    //기존에 등록되어 있는 내 정보 넣기
    private fun loadProfile(){

        if (G.userAccount?.imgfile.equals("") || G.userAccount?.imgfile == null){
            binding.ivProfile.setImageResource(R.drawable.profile)
        }else Glide.with(this).load(imgUrl).into(binding.ivProfile)
        binding.nicknameText.setText("${G.userAccount?.nickname}")
    }

    // 저장하기하면 앱에 프로필 이미지 저장하기
    fun saveSharedPreferences(){
        // SharedPreference 로 저장하기 - "Data.xml"파일에 저장해주는 객체를 소환하기
        val preferences: SharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
        // 저장작업 시작! -- 작성자 객체를 리턴해 줌
        val edior: SharedPreferences.Editor = preferences.edit()
        // 작성자를 통해 데이터를 작성
        edior.putString("imgfile",G.userAccount?.imgfile)
        edior.putString("nickname",G.userAccount?.nickname)
        edior.putString("password",G.userAccount?.password)
        edior.apply()

    }
}
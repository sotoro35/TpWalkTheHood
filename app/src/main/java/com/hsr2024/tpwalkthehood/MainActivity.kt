package com.hsr2024.tpwalkthehood

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.hsr2024.tpwalkthehood.adapter.PlaceItemAdapter
import com.hsr2024.tpwalkthehood.data.KakaoSearchPlaceResponse
import com.hsr2024.tpwalkthehood.data.Place
import com.hsr2024.tpwalkthehood.databinding.ActivityMainBinding
import com.hsr2024.tpwalkthehood.login.GuestFragment
import com.hsr2024.tpwalkthehood.login.LoginActivity
import com.hsr2024.tpwalkthehood.network.RetrofitHelper
import com.hsr2024.tpwalkthehood.network.RetrofitService
//import com.hsr2024.tpwalkthehood.tab1.Tab1WlakFragment
import com.hsr2024.tpwalkthehood.tab1.Tab1WlakFragmentTest
import com.hsr2024.tpwalkthehood.tab2.Tab2HoodFragment
import com.hsr2024.tpwalkthehood.tab3.Tab3FeedFragment
import com.hsr2024.tpwalkthehood.tab4.Tab4TalkFragment
import com.hsr2024.tpwalkthehood.tab5.Tab5MyFragment
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }


    // [위치작업] [ Google Fused Location API 사용 play - services - location]
    val locationProviderClient: FusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(this) }

    // [위치작업] 현재 내위치 정보 객체 (위도,경도 정보를 멤버로 보유)
    var myLocation: Location? = null


    // [검색작업] 카카오 검색된 내용을 갖고 있는 클래스
    var placeResponse:KakaoSearchPlaceResponse? = null


    val apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0"
    val key = "1TBrPTECd6gt7PR%2FK29IQYy7BEH1YV%2FQxqn8XOOg1ZQ7ujvcn1HCfL0ln4BYyF9jIHgGVq25bquADIFdixh3Mg%3D%3D"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.bnvView.itemIconTintList= null // 아이콘 색 넣으려고 설정..
        //binding.bnvView.background= null // 색 넣으려고 설정..

        // [바텀네비 별로 프래그먼트 보이도록 설정]
        supportFragmentManager.beginTransaction().add((R.id.container_fragment),Tab1WlakFragmentTest()).commit()

        binding.bnvView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_walk -> supportFragmentManager.beginTransaction().replace((R.id.container_fragment),Tab1WlakFragmentTest()).commit()
                R.id.menu_hood -> {
                    searchPlaces("FD6","음식점")
                    supportFragmentManager.beginTransaction().replace((R.id.container_fragment),Tab2HoodFragment()).commit()
                }
                R.id.menu_feed -> if (G.userAccount == null) supportFragmentManager.beginTransaction().replace((R.id.container_fragment),GuestFragment()).commit()
                else supportFragmentManager.beginTransaction().replace((R.id.container_fragment),Tab3FeedFragment()).commit()
                R.id.menu_talk -> if (G.userAccount == null) supportFragmentManager.beginTransaction().replace((R.id.container_fragment),GuestFragment()).commit()
                else supportFragmentManager.beginTransaction().replace((R.id.container_fragment),Tab4TalkFragment()).commit()
                R.id.menu_my -> if (G.userAccount == null) supportFragmentManager.beginTransaction().replace((R.id.container_fragment),GuestFragment()).commit()
                else supportFragmentManager.beginTransaction().replace((R.id.container_fragment),Tab5MyFragment()).commit()

            }
            true
        }// bnvView listener....



        // [위치작업] 위치정보 제공에 대한 퍼미션 체크 - 앱 최초실행시 1번만 권한요청
        val permissionState: Int = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionState == PackageManager.PERMISSION_DENIED){
            permissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        } else {
            requestMyLocation()
        }

        // 준비 단계 : base_date(발표 일자), base_time(발표 시각)
        // 현재 날짜, 시간 정보 가져오기
//        val cal = Calendar.getInstance()
//        base_date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time) // 현재 날짜
//        val timeH = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time) // 현재 시각
//        val timeM = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time) // 현재 분
//        // API 가져오기 적당하게 변환
//        base_time = getBaseTime(timeH, timeM)
//        // 현재 시각이 00시이고 45분 이하여서 baseTime이 2330이면 어제 정보 받아오기
//        if (timeH == "00" && base_time == "2330") {
//            cal.add(Calendar.DATE, -1).toString()
//            base_date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
//        }



    }// onCreate..

    private var base_date = "20210510"  // 발표 일자
    private var base_time = "1400"      // 발표 시각


    // 날씨 정보 가져오기
    // (한 페이지 결과 수 = 60, 페이지 번호 = 1, 응답 자료 형식-"JSON", 발표 날싸, 발표 시각, 예보지점 좌표)
    fun WeatherGet(){
        val retrofit = RetrofitHelper.getRetrofitInstance(apiUrl)
        val retrofitService = retrofit.create(RetrofitService::class.java)
        retrofitService.GetWeatherToString(60,1,"JSON","20240318","2100","${myLocation?.longitude}","${myLocation?.latitude}")
            .enqueue(object : Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    var s = response.body().toString()
                    AlertDialog.Builder(this@MainActivity).setMessage(s).create().show()
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "오류", Toast.LENGTH_SHORT).show()
                    Log.d("에러","${t.message}")
                }

            })
    }

    // baseTime 설정하기
    private fun getBaseTime(h : String, m : String) : String {
        var result = ""

        // 45분 전이면
        if (m.toInt() < 45) {
            // 0시면 2330
            if (h == "00") result = "2330"
            // 아니면 1시간 전 날씨 정보 부르기
            else {
                var resultH = h.toInt() - 1
                // 1자리면 0 붙여서 2자리로 만들기
                if (resultH < 10) result = "0" + resultH + "30"
                // 2자리면 그대로
                else result = resultH.toString() + "30"
            }
        }
        // 45분 이후면 바로 정보 받아오기
        else result = h + "30"

        return result
    }



    // [위치작업] 퍼미션을 받아올 대행사객체

    val permissionResult = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (it) requestMyLocation()
        else Toast.makeText(this, "내 위치정보를 제공하지 않아 검색기능 사용이 제한됩니다.", Toast.LENGTH_SHORT).show()
    }

    // [위치작업] 사용자의 위치 추적을 위한 설정
    @SuppressLint("SuspiciousIndentation")
    fun requestMyLocation(){

        //( 위치 정보의 정확도, 요청 간격 등)
    val request: LocationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,3000).build() //PRIORITY_HIGH_ACCURACY 높은정확도 gps


        //  사용자의 위치 정보에 접근하기 위한 권한이 있는지를 확인
        if (ActivityCompat.checkSelfPermission(
                this,
            Manifest.permission.ACCESS_FINE_LOCATION
            )!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ){
            return
        }

        // 위치 추척 시작
        locationProviderClient.requestLocationUpdates(request,locationCallback, Looper.getMainLooper())


    }// requestMyLocation....

    // [위치작업] 위치정보 갱신때마다 발동하는 콜백 객체
    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)

            myLocation = p0.lastLocation // 마지막 추척된 위치
            locationProviderClient.removeLocationUpdates(this) // 여기서 this는 콜백객체
            Toast.makeText(this@MainActivity, "${myLocation?.longitude}:${myLocation?.latitude}", Toast.LENGTH_SHORT).show()

            //차후 키워드 검색시... 파싱하는 작업 메소드 실행
            searchPlaces("FD6","음식점")
            //searchPlaces()
        }
    } // locationcallback....


    //[검색작업] 카카오 API 검색
    fun searchPlaces(searchCategory:String,searchKeyword:String){

        findViewById<ProgressBar>(R.id.progressBar)?.visibility = View.VISIBLE

        val retrofit = RetrofitHelper.getRetrofitInstance("https://dapi.kakao.com")
        val retrofitService = retrofit.create(RetrofitService::class.java)
        retrofitService.searchPlaceToKakao("$searchKeyword","${myLocation?.longitude}","${myLocation?.latitude}","$searchCategory").enqueue(
            object : Callback<KakaoSearchPlaceResponse>{
                override fun onResponse(
                    call: Call<KakaoSearchPlaceResponse>,
                    response: Response<KakaoSearchPlaceResponse>
                ) {
                    placeResponse = response.body()
                    val documents: List<Place>? = placeResponse?.documents

                    // 탭2 타이틀에 텍스트 변경하려고...
                    G.categoryG = searchCategory
                    G.keywordG = searchKeyword

                    val placeAdapter: PlaceItemAdapter
                    if (documents.isNullOrEmpty()) {
                        // documents가 비어 있을 경우, 빈 메시지를 표시합니다.
                        findViewById<TextView>(R.id.empty_view)?.visibility = View.VISIBLE
                        findViewById<RecyclerView>(R.id.recycler_sub_item)?.visibility = View.GONE
                        Toast.makeText(this@MainActivity, "검색중..", Toast.LENGTH_SHORT).show()

                        placeAdapter = PlaceItemAdapter(this@MainActivity, emptyList<Place>())
                        findViewById<RecyclerView>(R.id.recycler_sub_item)?.adapter = placeAdapter
                        placeAdapter.notifyDataSetChanged()

                    } else {
                        findViewById<RecyclerView>(R.id.recycler_sub_item)?.visibility = View.VISIBLE
                        findViewById<TextView>(R.id.empty_view)?.visibility = View.GONE

                        placeAdapter = PlaceItemAdapter(this@MainActivity, documents)
                        findViewById<RecyclerView>(R.id.recycler_sub_item)?.adapter = placeAdapter
                        placeAdapter.notifyDataSetChanged()

                    }

                    findViewById<ProgressBar>(R.id.progressBar)?.visibility = View.GONE

                }
                override fun onFailure(call: Call<KakaoSearchPlaceResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "오류${t.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }//searchPlaces

    fun searchPlacesMap(searchCategory:String,searchKeyword:String){
        val retrofit = RetrofitHelper.getRetrofitInstance("https://dapi.kakao.com")
        val retrofitService = retrofit.create(RetrofitService::class.java)
        retrofitService.searchPlaceToString("$searchKeyword","${myLocation?.longitude}","${myLocation?.latitude}","$searchCategory").enqueue(
            object : Callback<String>{
                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {
                    val s = response.body()
                    G.testmessage = s
                    G.categoryG = searchCategory
                    G.keywordG = searchKeyword

                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "오류${t.message}", Toast.LENGTH_SHORT).show()
                }

            }
        )


    }



    private fun searchPlaces(){
        val retrofit = RetrofitHelper.getRetrofitInstance("https://dapi.kakao.com")
        val retrofitService = retrofit.create(RetrofitService::class.java)
        retrofitService.searchPlaceToString("음식점","${myLocation?.longitude}","${myLocation?.latitude}","FD6").enqueue(
            object : Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val s = response.body()
                    AlertDialog.Builder(this@MainActivity).setMessage(s).create().show()
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "오류${t.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }//searchPlaces..........



}// main..
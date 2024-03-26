package com.hsr2024.tpwalkthehood

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Point
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.common.internal.service.Common
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hsr2024.tpwalkthehood.adapter.PlaceItemAdapter
import com.hsr2024.tpwalkthehood.adapter.WeatherAdapter
import com.hsr2024.tpwalkthehood.data.KakaoSearchPlaceResponse
import com.hsr2024.tpwalkthehood.data.ModelWeather
import com.hsr2024.tpwalkthehood.data.Place
import com.hsr2024.tpwalkthehood.data.Weather
import com.hsr2024.tpwalkthehood.data.Weatheritem
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
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    // [위치작업] [ Google Fused Location API 사용 play - services - location]
    val locationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this) }
    // 현재 위치의 격자 좌표를 저장할 포인트
    var curPoint:Point? = null

    // [위치작업] 현재 내위치 정보 객체 (위도,경도 정보를 멤버로 보유)
    var myLocation: Location? = null

    // [검색작업] 카카오 검색된 내용을 갖고 있는 클래스
    var placeResponse: KakaoSearchPlaceResponse? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.bnvView.itemIconTintList = null // 아이콘 색 넣으려고 설정..
        //binding.bnvView.background= null // 색 넣으려고 설정..

        // [바텀네비 별로 프래그먼트 보이도록 설정]
        supportFragmentManager.beginTransaction()
            .add((R.id.container_fragment), Tab1WlakFragmentTest()).commit()

        binding.bnvView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_walk -> {
                    supportFragmentManager.beginTransaction()
                        .replace((R.id.container_fragment), Tab1WlakFragmentTest(),).commit()
                    requestMyLocation()
                }

                R.id.menu_hood -> {
                    requestMyLocation()
                    supportFragmentManager.beginTransaction()
                        .replace((R.id.container_fragment), Tab2HoodFragment(),).commit()
                }

                R.id.menu_feed -> if (G.userAccount?.email == null || G.userAccount?.email == "") supportFragmentManager.beginTransaction()
                    .replace((R.id.container_fragment), GuestFragment(),).commit()
                else supportFragmentManager.beginTransaction()
                    .replace((R.id.container_fragment), Tab3FeedFragment()).commit()

                R.id.menu_talk -> if (G.userAccount?.email == null || G.userAccount?.email == "") supportFragmentManager.beginTransaction()
                    .replace((R.id.container_fragment), GuestFragment(),).commit()
                else supportFragmentManager.beginTransaction()
                    .replace((R.id.container_fragment), Tab4TalkFragment()).commit()

                R.id.menu_my -> if (G.userAccount?.email == null || G.userAccount?.email == "") supportFragmentManager.beginTransaction()
                    .replace((R.id.container_fragment), GuestFragment(),).commit()
                else supportFragmentManager.beginTransaction()
                    .replace((R.id.container_fragment), Tab5MyFragment()).commit()

            }
            true
        }// bnvView listener....


        // [위치작업] 위치정보 제공에 대한 퍼미션 체크 - 앱 최초실행시 1번만 권한요청
        val permissionState: Int = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionState == PackageManager.PERMISSION_DENIED) {
            permissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        } else { requestMyLocation() }

        // 플로팅버튼 클릭시 새로고침..
        binding.reload.setOnClickListener {

            when (binding.bnvView.selectedItemId) {
                R.id.menu_walk -> {
                    supportFragmentManager.beginTransaction()
                        .replace((R.id.container_fragment), Tab1WlakFragmentTest(),).commit()
                    requestMyLocation()
                }

                R.id.menu_hood -> {
                    supportFragmentManager.beginTransaction()
                        .replace((R.id.container_fragment), Tab2HoodFragment(),).commit()
                    searchPlaces("FD6", "음식점")
                }

                R.id.menu_feed -> if (G.userAccount?.email == null || G.userAccount?.email == "") supportFragmentManager.beginTransaction()
                    .replace((R.id.container_fragment), GuestFragment(),).commit()
                else {
                    supportFragmentManager.beginTransaction().replace((R.id.container_fragment), Tab3FeedFragment()).commit()
                    requestMyLocation()
                }

            }

            val animation = AnimationUtils.loadAnimation(this, R.anim.floatting_rotate)
            binding.reload.startAnimation(animation)

        }// reload....

    }// onCreate..


    override fun onResume() {
        super.onResume()
        if (L.login) binding.bnvView.selectedItemId = R.id.menu_walk
        L.login = false

    }



    // [위치작업] 퍼미션을 받아올 대행사객체

    val permissionResult = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (it) {
            requestMyLocation()
        } else Toast.makeText(this, "내 위치정보를 제공하지 않아 검색기능 사용이 제한됩니다.", Toast.LENGTH_SHORT).show()
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
            //Toast.makeText(this@MainActivity, "${myLocation?.longitude}:${myLocation?.latitude}", Toast.LENGTH_SHORT).show()

            curPoint = dfsXyConv(myLocation?.latitude?: 37.5666, myLocation?.longitude?: 126.9782)
            //차후 키워드 검색시... 파싱하는 작업 메소드 실행
            searchPlaces("FD6","음식점")
            //searchPlaces()

            // [날씨에게 좌표넘기기]
            val selectedItemId = findViewById<BottomNavigationView>(R.id.bnv_view).selectedItemId
            when (selectedItemId) {
               R.id.menu_walk -> {
                    val fragment =
                        supportFragmentManager.findFragmentById(R.id.container_fragment) as Tab1WlakFragmentTest
                    //AlertDialog.Builder(this@MainActivity).setMessage("${curPoint!!.x}:${curPoint!!.y}").create().show()
                    fragment.clickCategory(findViewById(R.id.category_01))
                    fragment.WeatherGet(curPoint!!.x.toString(), curPoint!!.y.toString())
                    // [ 동 구해오기 ]
                    fragment.regionNameRetrofit(
                        myLocation?.longitude.toString(),
                        myLocation?.latitude.toString()
                    )
                }

                R.id.menu_hood -> {
                    searchPlaces("FD6", "음식점")
                }

                R.id.menu_feed -> {
                    val fragment =
                        supportFragmentManager.findFragmentById(R.id.container_fragment) as Tab3FeedFragment
                    fragment.loadFeed()
                }
            }
        }
    } // locationcallback....

    // 위경도를 기상청에서 사용하는 격자 좌표로 변환
    //https://fre2-dom.tistory.com/429
    //https://blog.naver.com/PostView.naver?blogId=seoulworkshop&logNo=222873041453&categoryNo=11&parentCategoryNo=0

    fun dfsXyConv(v1: Double, v2: Double) : Point {
        val RE = 6371.00877     // 지구 반경(km)
        val GRID = 5.0          // 격자 간격(km)
        val SLAT1 = 30.0        // 투영 위도1(degree)
        val SLAT2 = 60.0        // 투영 위도2(degree)
        val OLON = 126.0        // 기준점 경도(degree)
        val OLAT = 38.0         // 기준점 위도(degree)
        val XO = 43             // 기준점 X좌표(GRID)
        val YO = 136            // 기준점 Y좌표(GRID)
        val DEGRAD = Math.PI / 180.0
        val re = RE / GRID
        val slat1 = SLAT1 * DEGRAD
        val slat2 = SLAT2 * DEGRAD
        val olon = OLON * DEGRAD
        val olat = OLAT * DEGRAD

        var sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn)
        var sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5)
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn
        var ro = Math.tan(Math.PI * 0.25 + olat * 0.5)
        ro = re * sf / Math.pow(ro, sn)

        var ra = Math.tan(Math.PI * 0.25 + (v1) * DEGRAD * 0.5)
        ra = re * sf / Math.pow(ra, sn)
        var theta = v2 * DEGRAD - olon
        if (theta > Math.PI) theta -= 2.0 * Math.PI
        if (theta < -Math.PI) theta += 2.0 * Math.PI
        theta *= sn

        val x = (ra * Math.sin(theta) + XO + 0.5).toInt()
        val y = (ro - ra * Math.cos(theta) + YO + 0.5).toInt()

        return Point(x, y)
    }


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


//    private fun searchPlaces(){
//        val retrofit = RetrofitHelper.getRetrofitInstance("https://dapi.kakao.com")
//        val retrofitService = retrofit.create(RetrofitService::class.java)
//        retrofitService.searchPlaceToString("음식점","${myLocation?.longitude}","${myLocation?.latitude}","FD6").enqueue(
//            object : Callback<String>{
//                override fun onResponse(call: Call<String>, response: Response<String>) {
//                    val s = response.body()
//                    AlertDialog.Builder(this@MainActivity).setMessage(s).create().show()
//                }
//                override fun onFailure(call: Call<String>, t: Throwable) {
//                    Toast.makeText(this@MainActivity, "오류${t.message}", Toast.LENGTH_SHORT).show()
//                }
//            }
//        )
//    }//searchPlaces..........


}// main..
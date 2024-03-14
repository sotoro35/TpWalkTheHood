package com.hsr2024.tpwalkthehood

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hsr2024.tpwalkthehood.databinding.ActivityMainBinding
import com.hsr2024.tpwalkthehood.login.GuestFragment
import com.hsr2024.tpwalkthehood.login.LoginActivity
import com.hsr2024.tpwalkthehood.tab1.Tab1WlakFragment
import com.hsr2024.tpwalkthehood.tab2.Tab2HoodFragment
import com.hsr2024.tpwalkthehood.tab3.Tab3FeedFragment
import com.hsr2024.tpwalkthehood.tab4.Tab4TalkFragment
import com.hsr2024.tpwalkthehood.tab5.Tab5MyFragment

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }


    // [ Google Fused Location API 사용 play - services - location]
    val locationProviderClient: FusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(this) }

    // 현재 내위치 정보 객체 (위도,경도 정보를 멤버로 보유)
    var myLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        binding.bnvView.itemIconTintList= null
        binding.bnvView.background= null

        supportFragmentManager.beginTransaction().add((R.id.container_fragment),Tab1WlakFragment()).commit()

        binding.bnvView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_walk -> supportFragmentManager.beginTransaction().replace((R.id.container_fragment),Tab1WlakFragment()).commit()
                R.id.menu_hood -> supportFragmentManager.beginTransaction().replace((R.id.container_fragment),Tab2HoodFragment()).commit()
                R.id.menu_feed -> if (G.userAccount == null) supportFragmentManager.beginTransaction().replace((R.id.container_fragment),GuestFragment()).commit()
                else supportFragmentManager.beginTransaction().replace((R.id.container_fragment),Tab3FeedFragment()).commit()
                R.id.menu_talk -> if (G.userAccount == null) supportFragmentManager.beginTransaction().replace((R.id.container_fragment),GuestFragment()).commit()
                else supportFragmentManager.beginTransaction().replace((R.id.container_fragment),Tab4TalkFragment()).commit()
                R.id.menu_my -> if (G.userAccount == null) supportFragmentManager.beginTransaction().replace((R.id.container_fragment),GuestFragment()).commit()
                else supportFragmentManager.beginTransaction().replace((R.id.container_fragment),Tab5MyFragment()).commit()

            }
            true
        }


        // 위치정보 제공에 대한 퍼미션 체크 - 앱 최초실행시 1번만 권한요청
        val permissionState: Int = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionState == PackageManager.PERMISSION_DENIED){

        }

//        val permissionState:Int = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) //퍼미션 수락 0 거부 -1
//        if (permissionState == PackageManager.PERMISSION_DENIED) {//거부되어 있을때 다이얼로그를 뜨게 함
//            //퍼미션을 요청하는 다이얼로그 보이고 그 결과를 받아오는 작업을 대신해주는 대행사 이용
//            permissionResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//
//        }else{
//            //위치정보 수집이 허가되어 있다면.. 곧바로 위치정보 얻어오는 작업 시작
//            requestMyLocation()
//        }



    }// onCreate..

    //퍼미션을 받아올 대행사객체

    val permissionResult = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (it) requestMyLocation()
        else Toast.makeText(this, "내 위치정보를 제공하지 않아 검색기능 사용이 제한됩니다.", Toast.LENGTH_SHORT).show()
    }

    private fun requestMyLocation(){

    }
}// main..
package com.hsr2024.tpwalkthehood.tab1

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hsr2024.tpwalkthehood.MainActivity
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.adapter.PlaceItemAdapter
import com.hsr2024.tpwalkthehood.adapter.WeatherAdapter
import com.hsr2024.tpwalkthehood.adapter.subCategoryTestAdapter
import com.hsr2024.tpwalkthehood.data.CategoryItem
import com.hsr2024.tpwalkthehood.data.KakaoRegionResponse
import com.hsr2024.tpwalkthehood.data.ModelWeather
import com.hsr2024.tpwalkthehood.data.Weather
import com.hsr2024.tpwalkthehood.data.Weatheritem
import com.hsr2024.tpwalkthehood.databinding.FragmentTab1testWlakBinding
import com.hsr2024.tpwalkthehood.network.RetrofitHelper
import com.hsr2024.tpwalkthehood.network.RetrofitService
import com.hsr2024.tpwalkthehood.tab2.Tab2HoodFragment
import com.kakao.sdk.common.KakaoSdk
import okhttp3.internal.notify
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Tab1WlakFragmentTest : Fragment(){

    private lateinit var binding:FragmentTab1testWlakBinding
    lateinit var main:MainActivity

    // [날씨작업]
    val apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/"

    private var base_date = "20240318"  // 발표 일자
    private var base_time = "1400"      // 발표 시각
    private var nx = "55"               // 예보지점 X 좌표
    private var ny = "127"              // 예보지점 Y 좌표

    //[날씨 api 값 넣을 변수
    var weatherResponse:Weather? = null

    //[날씨 api 값을 어댑터에 연결할 변수]


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTab1testWlakBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //대분류 클릭시 버튼에 클릭리스너
        setCategoryListener()

        main = activity as MainActivity

        clickCategory(binding.categoryBtns.category01)

        //main.placeResponse?: return
        if (main.placeResponse !== null ) {
            val placeAdapter = PlaceItemAdapter(requireContext(), main.placeResponse!!.documents)
            binding.recyclerSubItem.adapter = placeAdapter
            placeAdapter.notifyDataSetChanged()
        }

        // 날씨 클릭시 다이얼로그...
        try {
            binding.ivWeather.setOnClickListener {
                main.requestMyLocation()
                val dialogView = layoutInflater.inflate(R.layout.dialog_weather, null)
                val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recycler_weather)
                val builder = AlertDialog.Builder(requireContext())
                builder.setView(dialogView)

                val weatherAdapter = WeatherAdapter(requireContext(), weatherArr)
                recyclerView.adapter = weatherAdapter
                builder.show()
            }
        }catch ( e:Exception){
            AlertDialog.Builder(requireContext()).setMessage("다시 시도해주세요").create().show()
        }

    }//onViewCreated......

    override fun onResume() {
        super.onResume()
        binding.ivWeather.isEnabled = true
    }



    var weatherArr= mutableListOf(ModelWeather(), ModelWeather(), ModelWeather(), ModelWeather(), ModelWeather(), ModelWeather())

    //[날씨작업]
    // 날씨 정보 API 실행
    // (한 페이지 결과 수 = 60, 페이지 번호 = 1, 응답 자료 형식-"JSON", 발표 날싸, 발표 시각, 예보지점 좌표)
    fun WeatherGet(nx: String,ny: String){
        binding.ivWeather.isEnabled = false

        setWeather(nx, ny)

        val retrofit = RetrofitHelper.getRetrofitInstance(apiUrl)
        val retrofitService = retrofit.create(RetrofitService::class.java)
        retrofitService.GetWeather(60,1,"JSON",base_date,base_time,nx,ny)
            .enqueue(object : Callback<Weather>{
                override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                    weatherResponse = response.body()
                    var weatherItem:List<Weatheritem>? = weatherResponse?.response?.body?.items?.item



                    // 현재 시각부터 1시간 뒤의 날씨 6개를 담을 배열
                    //var weatherArr = mutableListOf(ModelWeather(), ModelWeather(), ModelWeather(), ModelWeather(), ModelWeather(), ModelWeather())

                    weatherItem?.apply {
                        // 배열채우기
                        var index = 0
                        val totalCount = response.body()!!.response.body.totalCount - 1
                        for (i in 0 .. totalCount){
                            index %= 6
                            when(weatherItem[i].category) {
                                "PTY" -> weatherArr[index].rainType = weatherItem[i].fcstValue
                                "SKY" -> weatherArr[index].sky = weatherItem[i].fcstValue
                                "T1H" -> weatherArr[index].temp = weatherItem[i].fcstValue
                                else -> continue
                            } // when....
                            index++
                        }// for....
                        // 각 날짜 배열 시간 설정
                        for (i in 0 .. 5) weatherArr[i].fcstTime = weatherItem[i].fcstTime

                        //AlertDialog.Builder(requireContext()).setMessage("${weatherArr[0].rainType}:${weatherArr[0].sky}").create().show()

                        binding.ivWeather.setImageResource(ModelWeather.getSky(weatherArr[0].rainType,weatherArr[0].sky))
                        binding.tvWeather.text = ModelWeather.getSkyString(weatherArr[0].rainType,weatherArr[0].sky)
                        binding.tvWeather2.text = "${weatherArr[0].temp}도"

                    } // apply.....

                    binding.ivWeather.isEnabled = true
                }// onResponse

                override fun onFailure(call: Call<Weather>, t: Throwable) {
                    Toast.makeText(requireContext(), "날씨파싱오류", Toast.LENGTH_SHORT).show()
                    Log.e("날씨","${t.message}")
                    binding.ivWeather.isEnabled = true
                }//onFailure
            })//retrofitService.....

    }

    fun showAlertDialog(){

    }

    // 날씨 정보 설정하기
    private fun setWeather(nx:String, ny:String) {
        // 멤버변수: base_date(발표 일자), base_time(발표 시각)

        // 현재 날짜, 시간 정보 가져오기

        val cal = Calendar.getInstance()
        base_date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
        var timeH = SimpleDateFormat("HH", Locale.getDefault()).format(cal.time)
        var timeM = SimpleDateFormat("mm", Locale.getDefault()).format(cal.time)

        //API 요청 문구로 변환
        base_time = getBaseTime(timeH,timeM)
        // 새벽1시라면 어제 정보 받아오기
        if(timeH == "00" && base_time == "2330"){
            cal.add(Calendar.DATE, -1).toString()
            base_date = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cal.time)
        }
    }

    // baseTime 설정하기 ( API 에 맞게 설정 )
    private fun getBaseTime(h : String, m : String) : String {
        // 매시간 45분마다 API 정보 제공시간. 현재시간 기준 45분이 되었는지 안되었는지에 맞춰 시각 변경
        // 요청시간은 무조건 매시각 30분으로 설정
        var result = ""

        // 45분 전이면
        if (m.toInt() < 45) {
            // 0시면 2330 // 전날 11시30분 요청
            if (h == "00") result = "2330"
            // 아니면 1시간 전 날씨 정보 부르기 // 발표전이라 전시각 발표 요청.
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

    // 위치에 해당하는 행정구역 받아오기
    fun regionNameRetrofit(x:String, y:String){
        val retrofit = RetrofitHelper.getRetrofitInstance("https://dapi.kakao.com")
        val retrofitService = retrofit.create(RetrofitService::class.java)
        retrofitService.regionName(x,y)
            .enqueue(object : Callback<KakaoRegionResponse>{
                override fun onResponse(
                    call: Call<KakaoRegionResponse>,
                    response: Response<KakaoRegionResponse>
                ) {
                    val regionResponse = response.body()
                    var regionItem = regionResponse?.documents
                    binding.tvRegion.text = "${regionItem?.get(0)?.region_2depth_name} ${regionItem?.get(0)?.region_3depth_name}"
                    //AlertDialog.Builder(requireContext()).setMessage("${regionItem?.get(0)?.region_2depth_name}").create().show()
                }

                override fun onFailure(call: Call<KakaoRegionResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "region오류", Toast.LENGTH_SHORT).show()
//                Log.e("region","${t.message}")
                }

            })
//            .enqueue(object : Callback<String>{
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                var s = response.body()
//                AlertDialog.Builder(requireContext()).setMessage(s).create().show()
//            }
//
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                Toast.makeText(requireContext(), "region오류", Toast.LENGTH_SHORT).show()
//                Log.e("region","${t.message}")
//            }
//
//        })

    }


    //멤버변수(property)
    var categoryId= R.id.category_01
    var searchCategory = ""
    var searchKeyword = ""
    var subsearchKeyword = ""

    lateinit var subsubcategoryAdapter:subCategoryTestAdapter

    private fun setCategoryListener(){

        binding.categoryBtns.category01.setOnClickListener { clickCategory(it) }
        binding.categoryBtns.category02.setOnClickListener { clickCategory(it) }
        binding.categoryBtns.category03.setOnClickListener { clickCategory(it) }
        binding.categoryBtns.category04.setOnClickListener { clickCategory(it) }
        binding.categoryBtns.category05.setOnClickListener { clickCategory(it) }
        binding.categoryBtns.category06.setOnClickListener { clickCategory(it) }
        binding.categoryBtns.category07.setOnClickListener { clickCategory(it) }
        binding.categoryBtns.category08.setOnClickListener { clickCategory(it) }


    } // listner......



    var SelectedCategory: Int = -1
     fun clickCategory(view:View){

        ///// 클릭 했을때 색상 변경 /////////////
        if (SelectedCategory != -1) {
            val prevView = binding.root.findViewById<TextView>(SelectedCategory)
            prevView?.setTextColor(Color.parseColor("#757474"))
        }
        // 현재 클릭한 텍스트뷰의 색상을 변경합니다.
        (view as TextView).setTextColor(Color.parseColor("#FF009688"))

        // 현재 클릭한 텍스트뷰의 id를 저장하여 다음에 선택 색상을 변경할 때 사용합니다.
        SelectedCategory = view.id


        //카테고리 설정
        // 클릭한 대분류에 해당하는 소분류 설정
        val clickedCategoryId =view.id
        val subcategoryData = when(clickedCategoryId){
            R.id.category_01 -> listOf(
                CategoryItem("전체보기",R.drawable.icon_all),
                CategoryItem("한식",R.drawable.icon_rice),
                CategoryItem("중식",R.drawable.icon_noodle),
                CategoryItem("일식",R.drawable.icon_sushi),
                CategoryItem("피자",R.drawable.icon_pizza),
                CategoryItem("치킨",R.drawable.icon_chicke),
                CategoryItem("분식",R.drawable.icon_ricecake),
                CategoryItem("애견동반",R.drawable.icon_pet)
            )
            R.id.category_02 -> listOf(
                CategoryItem("전체보기",R.drawable.icon_all),
                CategoryItem("애견동반",R.drawable.icon_pet)
            )
            R.id.category_03 -> listOf(
                CategoryItem("전체보기",R.drawable.icon_all),
                CategoryItem("어린이",R.drawable.icon_childre)
            )
            R.id.category_04 -> listOf(CategoryItem("전체보기",R.drawable.icon_all))
            R.id.category_05 -> listOf(CategoryItem("전체보기",R.drawable.icon_all))
            R.id.category_06 -> listOf(CategoryItem("전체보기",R.drawable.icon_all))
            R.id.category_07 -> listOf(
                CategoryItem("전체보기",R.drawable.icon_all),
                CategoryItem("내과",R.drawable.icon_stethoscope),
                CategoryItem("이비인후과",R.drawable.icon_otorhinolaryngology),
                CategoryItem("정형외과",R.drawable.icon_bone),
                CategoryItem("소아청소년과",R.drawable.icon_pediatric),
                CategoryItem("산부인과",R.drawable.icon_pregnant),
                CategoryItem("동물",R.drawable.companion_pet1)
            )
            R.id.category_08 -> listOf(
                CategoryItem("전체보기",R.drawable.icon_all),
                CategoryItem("동물",R.drawable.companion_pet1)
            )
            else -> emptyList()
        }

        //////////////////////////////////////////////////////////

        //클릭한 뷰의 id를 저장
        categoryId= view.id
        when(categoryId) {
            R.id.category_01 -> {
                searchCategory = "FD6"
                searchKeyword = "음식점"
            }
            R.id.category_02 -> {
                searchCategory = "CE7"
                searchKeyword = "카페"
            }
            R.id.category_03 -> {
                searchCategory = "CT1"
                searchKeyword = "문화시설"
            }
            R.id.category_04 -> {
                searchCategory = "CS2"
                searchKeyword = "편의점"
            }
            R.id.category_05 -> {
                searchCategory = "MT1"
                searchKeyword = "마트"
            }
            R.id.category_06 -> {
                searchCategory = "BK9"
                searchKeyword = "은행"
            }
            R.id.category_07 -> {
                searchCategory = "HP8"
                searchKeyword = "병원"
            }
            R.id.category_08 -> {
                searchCategory = "PM9"
                searchKeyword = "약국"
            }
        } // when...

        /////// 클릭시 검색하기 ///////////////////////
        main.searchPlaces(searchCategory,searchKeyword)

        // 서브 카테고리 어댑터를 업데이트합니다.
        subsubcategoryAdapter = subCategoryTestAdapter(requireContext(), subcategoryData) { clickedItem ->
            // 클릭한 서브 카테고리에 대한 동작 처리
            if (!clickedItem.category.equals("전체보기")) {
                subsearchKeyword = clickedItem.category
            } else {
                subsearchKeyword = searchKeyword
            }
            // 메인 액티비티의 searchPlaces 메소드 호출
            main.searchPlaces(searchCategory, subsearchKeyword)
        }

        // 리사이클러뷰에 어댑터 설정
        binding.reyclerViewSubCategory.adapter = subsubcategoryAdapter
        subsubcategoryAdapter.notifyDataSetChanged()
    }


//        private fun setupSubcategoryData(clickedCategoryId: Int): List<CategoryItem> {
//            return when(clickedCategoryId){
//                R.id.category_01 -> listOf(
//                    CategoryItem("전체보기",R.drawable.icon_all),
//                    CategoryItem("한식",R.drawable.icon_rice),
//                    CategoryItem("중식",R.drawable.icon_noodle),
//                    CategoryItem("일식",R.drawable.icon_sushi),
//                    CategoryItem("피자",R.drawable.icon_pizza),
//                    CategoryItem("치킨",R.drawable.icon_chicke),
//                    CategoryItem("분식",R.drawable.icon_ricecake),
//                    CategoryItem("애견동반",R.drawable.icon_pet)
//                )
//                R.id.category_02 -> listOf(
//                    CategoryItem("전체보기",R.drawable.icon_all),
//                    CategoryItem("애견동반",R.drawable.icon_pet)
//                )
//                R.id.category_03 -> listOf(
//                    CategoryItem("전체보기",R.drawable.icon_all),
//                    CategoryItem("어린이",R.drawable.icon_childre)
//                )
//                R.id.category_04 -> listOf(CategoryItem("전체보기",R.drawable.icon_all))
//                R.id.category_05 -> listOf(CategoryItem("전체보기",R.drawable.icon_all))
//                R.id.category_06 -> listOf(CategoryItem("전체보기",R.drawable.icon_all))
//                R.id.category_07 -> listOf(
//                    CategoryItem("전체보기",R.drawable.icon_all),
//                    CategoryItem("내과",R.drawable.icon_stethoscope),
//                    CategoryItem("이비인후과",R.drawable.icon_otorhinolaryngology),
//                    CategoryItem("정형외과",R.drawable.icon_bone),
//                    CategoryItem("소아청소년과",R.drawable.icon_pediatric),
//                    CategoryItem("산부인과",R.drawable.icon_pregnant),
//                    CategoryItem("동물",R.drawable.icon_pet)
//                )
//                R.id.category_08 -> listOf(
//                    CategoryItem("전체보기",R.drawable.icon_all),
//                    CategoryItem("동물",R.drawable.icon_pet)
//                )
//                else -> emptyList()
//            }
//        }
//
//        private fun setupSearchKeywords(categoryId: Int) {
//            when(categoryId) {
//                R.id.category_01 -> {
//                    searchCategory = "FD6"
//                    searchKeyword = "음식점"
//                }
//                R.id.category_02 -> {
//                    searchCategory = "CE7"
//                    searchKeyword = "카페"
//                }
//                R.id.category_03 -> {
//                    searchCategory = "CT1"
//                    searchKeyword = "문화시설"
//                }
//                R.id.category_04 -> {
//                    searchCategory = "CS2"
//                    searchKeyword = "편의점"
//                }
//                R.id.category_05 -> {
//                    searchCategory = "MT1"
//                    searchKeyword = "마트"
//                }
//                R.id.category_06 -> {
//                    searchCategory = "BK9"
//                    searchKeyword = "은행"
//                }
//                R.id.category_07 -> {
//                    searchCategory = "HP8"
//                    searchKeyword = "병원"
//                }
//                R.id.category_08 -> {
//                    searchCategory = "PM9"
//                    searchKeyword = "약국"
//                }
//            }
//        }

} // fragment..




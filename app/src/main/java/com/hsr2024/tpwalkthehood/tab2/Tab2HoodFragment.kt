package com.hsr2024.tpwalkthehood.tab2

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.marginLeft
import androidx.core.view.marginStart
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.hsr2024.tpwalkthehood.G
import com.hsr2024.tpwalkthehood.MainActivity
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.adapter.OptionSpinnerAdapter
import com.hsr2024.tpwalkthehood.data.Place
import com.hsr2024.tpwalkthehood.databinding.FragmentTab2HoodBinding
import com.hsr2024.tpwalkthehood.login.LoginActivity
import com.hsr2024.tpwalkthehood.tab1.ItemDetailActivity
import com.kakao.sdk.common.KakaoSdk
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraUpdate
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.mapwidget.InfoWindowOptions
import com.kakao.vectormap.mapwidget.component.GuiLayout
import com.kakao.vectormap.mapwidget.component.GuiText
import com.kakao.vectormap.mapwidget.component.Orientation
import java.lang.Exception


class Tab2HoodFragment : Fragment() {

    private val binding by lazy { FragmentTab2HoodBinding.inflate(layoutInflater) }

    lateinit var place:Place

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mapView.start(mapLifeCycleCallback,mapReadyCallbak)

        val main = activity as MainActivity


        binding.bottomsheet.setOnClickListener {
            val intent = Intent(requireContext(),ItemDetailActivity::class.java)
            val json:String = Gson().toJson(place)
            intent.putExtra("place",json)
            startActivity(intent)
        }//bottomsheet...


        val list = listOf<String>("음식점","카페","문화시설","편의점","마트","은행","병원","약국")

        binding.btnFilter.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(requireContext())
            val view = layoutInflater.inflate(R.layout.dialog_filter_bottom_sheet,null)

            var spinner = view.findViewById<Spinner>(R.id.spinner)

            var clickcategory:String = ""
            var clicksubKeyword:String = ""

            spinner.adapter = OptionSpinnerAdapter(requireContext(),R.layout.item_spinner_option,list)
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                   val value = spinner.getItemAtPosition(position).toString()
                    //Toast.makeText(requireContext(), value, Toast.LENGTH_SHORT).show()
                    clickcategory = value
                    clicksubKeyword = value
                    setupSearchCategory(clickcategory)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // 선택되지 않은경우
                }
            }

            view.findViewById<TextView>(R.id.btn_spinner).setOnClickListener {
                G.categoryG = serchCategory
                G.keywordG = clicksubKeyword
                Toast.makeText(requireContext(), "${G.categoryG}:${G.keywordG}", Toast.LENGTH_SHORT).show()
                main.searchPlaces(G.categoryG, G.keywordG)
                binding.mapView.start(mapLifeCycleCallback,mapReadyCallbak)
                bottomSheetDialog.dismiss()

            }

            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()
        }



    }//onViewCreated

    var serchCategory:String = ""
    var serchKeyword:String = ""

    val mapLifeCycleCallback:MapLifeCycleCallback = object : MapLifeCycleCallback(){
        override fun onMapDestroy(){}
        override fun onMapError(p0: Exception?) {}
    }


    val mapReadyCallbak : KakaoMapReadyCallback = object : KakaoMapReadyCallback() {
        override fun onMapReady(kakaoMap: KakaoMap) { // 지도 가져오는게 완료되면 콜백객체 발동

            // 내 위치 설정
            val latitude:Double = (activity as MainActivity).myLocation?.latitude ?: 37.5666
            val longitude:Double = (activity as MainActivity).myLocation?.longitude ?: 126.9782
            val myPos:LatLng = LatLng.from(latitude,longitude)

            // 내 위치로 카메라 이동
            val cameraUpdate:CameraUpdate = CameraUpdateFactory.newCenterPosition(myPos,16) // 지도의 가운데 지점을 내 위치로 이동!
            kakaoMap.moveCamera(cameraUpdate)

            // 내 위치에 마커(라벨) 추가
            val labelOption : LabelOptions = LabelOptions.from(myPos).setStyles(R.drawable.pin_y)
            val labelLayer: LabelLayer = kakaoMap.labelManager!!.layer!! //레이어 객체 생성
            labelLayer.addLabel(labelOption) // 레이어 객체에 내 위치 마커옵션 넣기

            var options: LabelOptions
            var placePos: LatLng


            // 주변 위치 마커 추가
            val placeList: List<Place>? = (activity as MainActivity).placeResponse?.documents
            binding.hoodTitle.text = "동네 [ ${G.keywordG} ] 찾았어요!"
            placeList?.forEach{
                placePos = LatLng.from(it.y.toDouble(), it.x.toDouble())
                options = LabelOptions.from(placePos).setStyles(R.drawable.pin_g).setTexts("${it.distance}M").setTag(it)
                kakaoMap.labelManager!!.layer!!.addLabel(options)
            }// forEach....

            var behavior = BottomSheetBehavior.from(binding.bottomsheet)


            var previouslyClickedLabel: Label? = null

            kakaoMap.setOnLabelClickListener { kakaoMap, labelLayer, label ->

                label.apply {

                    label.setStyles(R.drawable.pin_g)
                    val layout = GuiLayout(Orientation.Vertical)
                    layout.setPadding(16,16,16,16)
                    layout.setBackground(R.drawable.pin_g2,true)

                    this.texts.forEach {
                        val guiText = GuiText(it)
                        guiText.setTextSize(30)
                        guiText.setTextColor(Color.GREEN)
                        layout.addView(guiText)
                    }

                    val options = InfoWindowOptions.from(label.position)
                    options.body = layout
                    options.setBodyOffset(0f,0f)
                    options.setTag(this.tag)

                    kakaoMap.mapWidgetManager!!.infoWindowLayer.removeAll()
                   kakaoMap.mapWidgetManager!!.infoWindowLayer.addInfoWindow(options)

                    place= tag as Place
                    binding.hoodPlaceName.text = place.place_name
                    binding.hoodAddressName.text= place.address_name
                    binding.distance.text = "${place.distance}M"

                }//label.apply

                behavior.state = BottomSheetBehavior.STATE_EXPANDED


                //var labelStyle=  LabelStyle.from(R.drawable.pin_g)
                //                var labelStyle2=  LabelStyle.from(R.drawable.pin_y)
                //                // 이전에 클릭된 레이블이 있으면 해당 레이블의 스타일을 원래대로 복원
                //                previouslyClickedLabel?.changeStyles(LabelStyles.from(labelStyle))
                //                // 현재 클릭된 레이블의 스타일 변경
                //                label.changeStyles(LabelStyles.from(labelStyle2))
                //                // 현재 클릭된 레이블을 이전에 클릭된 레이블로 설정
                //                previouslyClickedLabel = label

            }// setOnLabelClickListener....
        }// onMap
    } //callback

    private fun setupSearchCategory(category:String) {
        when(category){
            "음식점" -> serchCategory = "FD6"
            "카페" -> serchCategory = "CE7"
            "문화시설" -> serchCategory = "CT1"
            "편의점" -> serchCategory = "CS2"
            "마트" -> serchCategory = "MT1"
            "은행" -> serchCategory = "BK9"
            "병원" -> serchCategory = "HP8"
            "약국" -> serchCategory = "PM9"

        }
    }



}



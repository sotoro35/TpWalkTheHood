package com.hsr2024.tpwalkthehood.tab5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hsr2024.tpwalkthehood.G
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.adapter.PlaceItemAdapter
import com.hsr2024.tpwalkthehood.adapter.favoriteAdapter
import com.hsr2024.tpwalkthehood.data.Place
import com.hsr2024.tpwalkthehood.data.favoriteItem
import com.hsr2024.tpwalkthehood.databinding.ActivityFavoriteListBinding

// 찜목록... 아이템상세에서 찜한것들을 보여주는곳. 찜은 계정저장(닷홈.. 파이어베이스의 계정과 비교해서..), 폰에저장
// 삭제기능도 있음
class FavoriteListActivity : AppCompatActivity() {

    val binding by lazy { ActivityFavoriteListBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.tvNicknameFavor.text = "' ${G.userAccount?.nickname?: "사용자"} ' 님의 찜 목록"


    }

    override fun onResume() {
        super.onResume()
        loadFavorite(G.userAccount!!.email)
    }

    private fun loadFavorite(userEmail: String){

        val db = Firebase.firestore

        // 찜 목록 가져오기
            db.collection("favorites")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnSuccessListener { querySnapshot ->

                    var placeList = mutableListOf<Place>()

                    if (!querySnapshot.isEmpty) {
                        for (document in querySnapshot.documents) {
                            // 찜 목록에 대한 작업 수행
                            var id = document.getString("id")
                            var placeName = document.getString("place_name")
                            var category = document.getString("category_name")
                            var phone = document.getString("phone")
                            var address = document.getString("address_name")
                            var roadAddress = document.getString("road_address_name")
                            var x = document.getString("x")
                            var y = document.getString("y")
                            var url = document.getString("place_url")
                            var distance = document.getString("place_distance")

                            // FavoriteItem 객체 생성
                            var placeItem = Place(id?:"",placeName?:"", category?:"", phone?:"", address?:"",
                                roadAddress?:"", x?:"", y?:"", url?:"",distance?:"")
                            // 리스트에 추가
                            placeList.add(placeItem)
                        }
                    } else {
                        // 찜 목록이 비어 있는 경우 처리
                        Log.d("찜 목록", "찜 목록이 비어 있습니다.")
                    }

                    val adapter = PlaceItemAdapter(this@FavoriteListActivity,placeList)
                    binding.recyclerviewFavor.adapter = adapter

                }

        //"place_name" to "${place.place_name}",
        //            "category_name" to "${place.category_name}",
        //            "phone" to "${place.phone}",
        //            "address_name" to "${place.address_name}",
        //            "road_address_name" to "${place.road_address_name}",
        //            "x" to "${place.x}",
        //            "y" to "${place.y}",
        //            "place_distance" to "${place.distance}",
        //            "place_url" to "${place.place_url}",
        //            "email" to "${G.userAccount!!.email}",
    }
}
package com.hsr2024.tpwalkthehood.tab5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hsr2024.tpwalkthehood.G
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.adapter.favoriteAdapter
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
        loadFavorite(G.userAccount!!.email)

    }

    private fun loadFavorite(userEmail: String){

        val db = Firebase.firestore

        // 찜 목록 가져오기
            db.collection("favorites")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnSuccessListener { querySnapshot ->

                    val favoriteItemList = mutableListOf<favoriteItem>()

                    if (!querySnapshot.isEmpty) {
                        for (document in querySnapshot.documents) {
                            // 찜 목록에 대한 작업 수행
                            val placeName = document.getString("place_name")
                            val roadAddress = document.getString("road_address_name")
                            val distance = document.getString("distance")
                            val url = document.getString("url")
                            // FavoriteItem 객체 생성
                            val favoriteItem = favoriteItem(placeName, roadAddress, distance, url)
                            // 리스트에 추가
                            favoriteItemList.add(favoriteItem)
                        }
                    } else {
                        // 찜 목록이 비어 있는 경우 처리
                        Log.d("찜 목록", "찜 목록이 비어 있습니다.")
                    }

                    val adapter = favoriteAdapter(this@FavoriteListActivity,favoriteItemList)
                    binding.recyclerviewFavor.adapter = adapter

                }
    }
}
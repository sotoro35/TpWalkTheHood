package com.hsr2024.tpwalkthehood.tab3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.hsr2024.tpwalkthehood.G
import com.hsr2024.tpwalkthehood.MainActivity
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.adapter.Tab3FeedAdapter
import com.hsr2024.tpwalkthehood.data.FeedItem
import com.hsr2024.tpwalkthehood.databinding.FragmentTab3FeedBinding
import com.hsr2024.tpwalkthehood.tab5.ChangeProfileActivity


// 리사이클러뷰에 서버에 저장되어있는 게시물들 가져오기
// 글쓰기 버튼 누르면 EditActivity로 이동
// 위에로 아래로 내리면 전체 새로고침
// 게시물 클릭시 해당 게시물의 FeedDetail로 이동
// 리사이클러뷰에 보여줘야 할 아이템 = 프로필사진,닉네임,제목,좋아요수,댓글수
class Tab3FeedFragment : Fragment(){

    private val binding by lazy { FragmentTab3FeedBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edit.setOnClickListener {
            startActivity(Intent(requireContext(), EditActivity::class.java)
        ) }

        //Toast.makeText(requireContext(), "${G.userAccount?.imgfile}", Toast.LENGTH_SHORT).show()

    }

    override fun onResume() {
        super.onResume()
        loadFeed()

    }

  fun loadFeed(){

        val loadRef = Firebase.firestore.collection("Posts")
        loadRef.get().addOnSuccessListener {querySnapshot->
            val postList = mutableListOf<FeedItem>()
            var postitem:FeedItem

            for (document in querySnapshot.documents){
                var email: String = document.getString("email")!!
                var nickname: String? = document.getString("nickname")?: "닉넴없음"
                var title:String = document.getString("title")!!
                var text:String = document.getString("text")!!
                var date:String = document.getString("date")!!
                var downUrl:String = document.getString("downUrl")?: "1"
                var profile:String = document.getString("profile")?: "1"
                var fileName:String = document.getString("fileName")?: "1"

                postitem = FeedItem(email, nickname, title, text, date, downUrl, profile, fileName)
                postList.add(0,postitem)
            }

            val adapter = Tab3FeedAdapter(requireContext(),postList)
            binding.reyclerviewTab3.adapter = adapter
            adapter.notifyDataSetChanged()


            Log.w("성공", "포스트 가져오기성공")
        }
            .addOnFailureListener {exception ->
                // 데이터 가져오기 실패 시 처리
                Log.w("오류Feed", "Error getting documents: ", exception)

            }
    }
}

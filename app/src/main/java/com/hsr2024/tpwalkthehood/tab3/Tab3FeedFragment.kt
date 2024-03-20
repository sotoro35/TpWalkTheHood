package com.hsr2024.tpwalkthehood.tab3

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

        binding.edit.setOnClickListener { startActivity(
            Intent(requireContext(),
                EditActivity::class.java)
        ) }
    }
}
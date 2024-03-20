package com.hsr2024.tpwalkthehood.tab4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hsr2024.tpwalkthehood.databinding.FragmentTab3FeedBinding
import com.hsr2024.tpwalkthehood.databinding.FragmentTab4TalkBinding

// 친구목록을 보여줌 서버에서 가져옴 프로필사진/닉네임
// 친구를 클릭하면 채팅방으로 이동
// 상단의 요청알림 클릭시 친구신청 알림창이뜸
// 친구설정창을 누르면 삭제 및 친구게시물로 이동


class Tab4TalkFragment : Fragment() {

    private val binding by lazy { FragmentTab4TalkBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}
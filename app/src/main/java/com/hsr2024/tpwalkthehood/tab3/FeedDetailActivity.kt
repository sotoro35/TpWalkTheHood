package com.hsr2024.tpwalkthehood.tab3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.hsr2024.tpwalkthehood.FeedString
import com.hsr2024.tpwalkthehood.G
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.databinding.ActivityFeedDetailBinding

// Feed에서 아이템 클릭시 보여주는 상세내역
// 서버에서 내용 가져와야함
// 좋아요 / 댓글 / 친구신청 클릭시 서버로 정보 전달

class FeedDetailActivity : AppCompatActivity() {

    val binding by lazy {ActivityFeedDetailBinding.inflate(layoutInflater) }

    var imgUrl= "http://ruaris.dothome.co.kr/WalkTheHood/${FeedString.profile}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.nickname.text = FeedString.nickname
        binding.tvFeedTitle.text = FeedString.title
        binding.tvFeedText.text =FeedString.text
        binding.feedIv.visibility = View.VISIBLE

        if(FeedString.profile == null || FeedString.profile == "1"){
            binding.ivProfile.setImageResource(R.drawable.profile)
        }else Glide.with(this).load(imgUrl).into(binding.ivProfile)

        if (FeedString.downUrl == null || FeedString.downUrl == "1"){
            binding.feedIv.visibility = View.GONE
        } else {
            binding.feedIv.visibility = View.VISIBLE
            Glide.with(this).load(FeedString.downUrl).into(binding.feedIv)
        }



    }//onCreate...

}
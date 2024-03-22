package com.hsr2024.tpwalkthehood.tab5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hsr2024.tpwalkthehood.G
import com.hsr2024.tpwalkthehood.R
import com.hsr2024.tpwalkthehood.adapter.Tab3FeedAdapter
import com.hsr2024.tpwalkthehood.data.FeedItem
import com.hsr2024.tpwalkthehood.databinding.ActivityEditMyFeedBinding

class EditMyFeed : AppCompatActivity() {

    val bindig by lazy { ActivityEditMyFeedBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindig.root)

        bindig.toolbar.setNavigationOnClickListener { finish() }


    }


}
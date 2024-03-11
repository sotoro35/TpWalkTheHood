package com.hsr2024.tpwalkthehood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.get
import com.hsr2024.tpwalkthehood.databinding.ActivityMainBinding
import com.hsr2024.tpwalkthehood.tab1.Tab1WlakFragment
import com.hsr2024.tpwalkthehood.tab2.Tab2HoodFragment
import com.hsr2024.tpwalkthehood.tab3.Tab3FeedFragment
import com.hsr2024.tpwalkthehood.tab4.Tab4TalkFragment
import com.hsr2024.tpwalkthehood.tab5.Tab5MyFragment

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.bnvView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_walk -> supportFragmentManager.beginTransaction().replace((R.id.container_fragment),Tab1WlakFragment()).commit()
                R.id.menu_hood -> supportFragmentManager.beginTransaction().replace((R.id.container_fragment),Tab2HoodFragment()).commit()
                R.id.menu_feed -> supportFragmentManager.beginTransaction().replace((R.id.container_fragment),Tab3FeedFragment()).commit()
                R.id.menu_talk -> supportFragmentManager.beginTransaction().replace((R.id.container_fragment),Tab4TalkFragment()).commit()
                R.id.menu_my -> supportFragmentManager.beginTransaction().replace((R.id.container_fragment),Tab5MyFragment()).commit()

            }



            true
        }





    }
}
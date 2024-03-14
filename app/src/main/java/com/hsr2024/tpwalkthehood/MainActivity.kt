package com.hsr2024.tpwalkthehood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import com.hsr2024.tpwalkthehood.databinding.ActivityMainBinding
import com.hsr2024.tpwalkthehood.login.GuestFragment
import com.hsr2024.tpwalkthehood.login.LoginActivity
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



        binding.bnvView.itemIconTintList= null
        binding.bnvView.background= null

        supportFragmentManager.beginTransaction().add((R.id.container_fragment),Tab1WlakFragment()).commit()

        AlertDialog.Builder(this).setMessage("${G.userAccount?.password}").create().show()

        binding.bnvView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_walk -> supportFragmentManager.beginTransaction().replace((R.id.container_fragment),Tab1WlakFragment()).commit()
                R.id.menu_hood -> supportFragmentManager.beginTransaction().replace((R.id.container_fragment),Tab2HoodFragment()).commit()
                R.id.menu_feed -> if (G.userAccount == null) supportFragmentManager.beginTransaction().replace((R.id.container_fragment),GuestFragment()).commit()
                else supportFragmentManager.beginTransaction().replace((R.id.container_fragment),Tab3FeedFragment()).commit()
                R.id.menu_talk -> if (G.userAccount == null) supportFragmentManager.beginTransaction().replace((R.id.container_fragment),GuestFragment()).commit()
                else supportFragmentManager.beginTransaction().replace((R.id.container_fragment),Tab4TalkFragment()).commit()
                R.id.menu_my -> if (G.userAccount == null) supportFragmentManager.beginTransaction().replace((R.id.container_fragment),GuestFragment()).commit()
                else supportFragmentManager.beginTransaction().replace((R.id.container_fragment),Tab5MyFragment()).commit()

            }
            true
        }





    }
}
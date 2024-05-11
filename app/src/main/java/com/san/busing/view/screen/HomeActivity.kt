package com.san.busing.view.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.san.busing.R
import com.san.busing.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNav()
    }

    private fun initBottomNav() {
        add(SearchRouteFragment())   // 첫 화면 지정

        binding.btmNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navRoute -> replaceTo(SearchRouteFragment())
                R.id.navTest -> replaceTo(TestFragment())
            }

            return@setOnItemSelectedListener true
        }
    }

    private fun add(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(binding.flHome.id, fragment)
        transaction.commit()
    }

    private fun replaceTo(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.flHome.id, fragment)
        transaction.commit()
    }
}
package com.san.busing.view.screen

import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.san.busing.R
import com.san.busing.SettingFragment
import com.san.busing.databinding.ActivityHomeBinding
import com.san.busing.view.screen.route.SearchRouteFragment
import com.san.busing.view.screen.station.SearchStationFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var backClicked = false
    private var onMainFragment = true
    private lateinit var finishWaitingToast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNav()
        initFinishWaitingToast()
    }

    private fun initBottomNav() {
        add(SearchRouteFragment())
        supportFragmentManager.addFragmentOnAttachListener { _, fragment ->
            onMainFragment = fragment is SearchRouteFragment
        }

        binding.btmNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navRoute -> replaceTo(SearchRouteFragment())
                R.id.navStation -> replaceTo(SearchStationFragment())
                R.id.navSetting -> replaceTo(SettingFragment())
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

    private fun initFinishWaitingToast() {
        finishWaitingToast = Toast.makeText(this, FINISH_WAITING_MESSAGE, Toast.LENGTH_SHORT).also {
            it.addCallback(object: Toast.Callback() {
                override fun onToastShown() {
                    backClicked = true
                    super.onToastShown()
                }

                override fun onToastHidden() {
                    backClicked = false
                    super.onToastHidden()
                }
            })
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!onMainFragment) {
                replaceTo(SearchRouteFragment())
                binding.btmNav.selectedItemId = R.id.navRoute
                return true
            } else {
                if (backClicked) finish()
                else {
                    finishWaitingToast.show()
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        private const val FINISH_WAITING_MESSAGE = "종료를 원하시면 '뒤로'버튼을 한번 더 눌러주세요"
    }
}
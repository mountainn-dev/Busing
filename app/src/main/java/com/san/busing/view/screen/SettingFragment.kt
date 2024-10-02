package com.san.busing.view.screen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.san.busing.BuildConfig
import com.san.busing.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSettingBinding.inflate(layoutInflater)

        initListener()

        return binding.root
    }

    private fun initListener() {
        setBtnPrivateInfoTermListener()
    }

    private fun setBtnPrivateInfoTermListener() {
        binding.btnPrivateInfoTerm.setOnClickListener { sendUserToPrivateInfoTermWebSite() }
    }

    private fun sendUserToPrivateInfoTermWebSite() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.PRIVATE_INFO_TERM_URL))

        startActivity(intent)
    }
}
package com.san.busing.view.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.san.busing.BuildConfig
import com.san.busing.data.repositoryimpl.StationRepositoryImpl
import com.san.busing.data.source.remote.retrofit.StationService
import com.san.busing.data.vo.Id
import com.san.busing.databinding.ActivityStationDetailBinding
import com.san.busing.domain.utils.Const
import com.san.busing.domain.utils.Utils
import com.san.busing.view.viewmodel.StationDetailViewModel
import com.san.busing.view.viewmodelfactory.StationDetailViewModelFactory
import com.san.busing.view.viewmodelimpl.StationDetailViewModelImpl

class StationDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStationDetailBinding
    private lateinit var viewModel: StationDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = StationRepositoryImpl(
            Utils.getRetrofit(BuildConfig.STATION_URL).create(StationService::class.java),
            this.applicationContext
        )
        val stationId = intent.getSerializableExtra(Const.TAG_STATION_ID) as Id
        val stationMobileNo = intent.getStringExtra(Const.TAG_STATION_MOBILE_NUMBER) ?: Const.EMPTY_TEXT
        val stationName = intent.getStringExtra(Const.TAG_STATION_NAME) ?: Const.EMPTY_TEXT
        val regionName = intent.getStringExtra(Const.TAG_REGION_NAME) ?: Const.EMPTY_TEXT
        viewModel = ViewModelProvider(
            this, StationDetailViewModelFactory(repository, stationId, stationMobileNo, stationName, regionName)
        ).get(StationDetailViewModelImpl::class.java)

        viewModel.updateRecentSearch(this)
        initAppBar(stationName, stationMobileNo, regionName)
    }

    private fun initAppBar(
        name: String, mobileNo: String, regionName: String
    ) {
        setTitle(name)
        setContent(name, mobileNo, regionName)
    }

    private fun setTitle(stationName: String) {
        binding.txtTitle.text = stationName
    }

    private fun setContent(
        name: String, mobileNo: String, regionName: String
    ) {
        binding.txtStationName.text = name
        binding.txtStationMobileNo.text = mobileNo
        binding.txtRegionName.text = regionName
    }
}
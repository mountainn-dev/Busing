package com.san.busing.view.screen

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.san.busing.BuildConfig
import com.san.busing.R
import com.san.busing.data.repositoryimpl.BusArrivalRepositoryImpl
import com.san.busing.data.repositoryimpl.StationRepositoryImpl
import com.san.busing.data.source.remote.retrofit.BusArrivalService
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

        val stationRepository = StationRepositoryImpl(
            Utils.getRetrofit(BuildConfig.STATION_URL).create(StationService::class.java),
            Utils.getRetrofit(BuildConfig.ARRIVAL_URL).create(BusArrivalService::class.java),
            this.applicationContext
        )
        val stationId = intent.getSerializableExtra(Const.TAG_STATION_ID) as Id
        val stationMobileNo = intent.getStringExtra(Const.TAG_STATION_MOBILE_NUMBER) ?: Const.EMPTY_TEXT
        val stationName = intent.getStringExtra(Const.TAG_STATION_NAME) ?: Const.EMPTY_TEXT
        val regionName = intent.getStringExtra(Const.TAG_REGION_NAME) ?: Const.EMPTY_TEXT
        viewModel = ViewModelProvider(
            this, StationDetailViewModelFactory(stationRepository, stationId, stationMobileNo, stationName, regionName)
        ).get(StationDetailViewModelImpl::class.java)

        viewModel.updateRecentSearch(this)
        initToolbar(stationName, stationMobileNo, regionName)
        initObserver(this)
        initListener(this)
    }

    private fun initToolbar(
        name: String, mobileNo: String, regionName: String
    ) {
        setTitle(name)
        setContent(name, mobileNo, regionName)
        startEllipsizeMarqueeEffect()
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

    private fun startEllipsizeMarqueeEffect() {
        binding.txtTitle.setHorizontallyScrolling(true)
        binding.txtTitle.isSelected = true
    }

    private fun initObserver(activity: Activity) {
        viewModel.resetTimer.observe(
            activity as LifecycleOwner,
            resetTimerObserver()
        )
        viewModel.bookMark.observe(
            activity as LifecycleOwner,
            bookMarkObserver()
        )
    }

    private fun resetTimerObserver() = Observer<Int> {
        if (it == Const.ZERO) {
            binding.fabRefresh.setImageResource(R.drawable.ic_refresh)
            binding.fabTime.visibility = View.GONE
        } else {
            if (binding.fabTime.visibility == View.GONE) {
                binding.fabTime.visibility = View.VISIBLE
                binding.fabRefresh.setImageResource(android.R.color.transparent)
            }
            binding.fabTime.text = it.toString()
        }
    }

    private fun bookMarkObserver() = Observer<Boolean> {
        if (it) binding.btnBookMark.setImageResource(R.drawable.ic_on_book_mark)
        else binding.btnBookMark.setImageResource(R.drawable.ic_off_book_mark)
    }

    private fun initListener(activity: Activity) {
        setBtnBackListener()
        setBtnBookMarkListener(activity)
        setBtnRequestListener()
        setFabRefreshListener()
    }

    private fun setBtnBackListener() {
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun setBtnBookMarkListener(activity: Activity) {
        binding.btnBookMark.setOnClickListener {
            if (viewModel.bookMark.value!!) Toast.makeText(activity, BOOKMARK_UNREGISTER_MESSAGE, Toast.LENGTH_SHORT).show()
            else Toast.makeText(activity, BOOKMARK_REGISTER_MESSAGE, Toast.LENGTH_SHORT).show()
            viewModel.toggleBookMark()
        }
    }

    private fun setBtnRequestListener() {
        binding.btnTimeoutRequest.setOnClickListener { viewModel.load() }
        binding.btnServiceErrorRequest.setOnClickListener { viewModel.load() }
    }

    private fun setFabRefreshListener() {
        binding.fabRefresh.setOnClickListener { viewModel.loadWithTimer() }
    }

    override fun onResume() {
        super.onResume()
        viewModel.load()
    }

    companion object {
        private const val BOOKMARK_REGISTER_MESSAGE = "즐겨찾기가 등록되었습니다."
        private const val BOOKMARK_UNREGISTER_MESSAGE = "즐겨찾기가 해제되었습니다."
    }
}
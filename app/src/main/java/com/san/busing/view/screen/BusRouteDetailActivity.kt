package com.san.busing.view.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.san.busing.BuildConfig
import com.san.busing.data.repositoryimpl.BusRouteRepositoryImpl
import com.san.busing.databinding.ActivityBusRouteDetailBinding
import com.san.busing.domain.model.BusRouteRecentSearchModel
import com.san.busing.domain.utils.Const
import com.san.busing.domain.utils.Utils
import com.san.busing.domain.viewmodel.BusRouteDetailViewModel
import com.san.busing.domain.viewmodelfactory.BusRouteDetailViewModelFactory
import com.san.busing.domain.viewmodelimpl.BusRouteDetailViewModelImpl

class BusRouteDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBusRouteDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusRouteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = BusRouteRepositoryImpl(Utils.getRetrofit(BuildConfig.ROUTES_URL), this.applicationContext)
        val recentSearchModel = intent.getSerializableExtra(Const.TAG_ROUTE_ID) as BusRouteRecentSearchModel
        val viewModel = ViewModelProvider(this, BusRouteDetailViewModelFactory(repository, recentSearchModel)).get(
            BusRouteDetailViewModelImpl::class.java
        )

        initObserver(viewModel)
        initListener(viewModel)
    }

    private fun initObserver(viewModel: BusRouteDetailViewModel) {
        viewModel.routeInfoReady.observe(this, routeInfoReadyObserver(viewModel))
    }

    private fun routeInfoReadyObserver(viewModel: BusRouteDetailViewModel) = Observer<Boolean> {
        if (it) {
            binding.txtRouteName.text = viewModel.routeInfo.name
            binding.txtRouteStartStation.text = viewModel.routeInfo.startStationName
            binding.txtRouteEndStation.text = viewModel.routeInfo.endStationName
        } else {
            binding.txtRouteName.text = Const.EMPTY_TEXT
            binding.txtRouteStartStation.text = Const.EMPTY_TEXT
            binding.txtRouteEndStation.text = Const.EMPTY_TEXT
        }
    }

    private fun initListener(viewModel: BusRouteDetailViewModel) {
        setFabRefreshListener(viewModel)
    }

    private fun setFabRefreshListener(viewModel: BusRouteDetailViewModel) {
        binding.fabRefresh.setOnClickListener { viewModel.loadContent() }
    }
}
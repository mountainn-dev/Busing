package com.san.busing.view.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.san.busing.BuildConfig
import com.san.busing.data.repositoryimpl.BusRouteRepositoryImpl
import com.san.busing.data.type.Id
import com.san.busing.databinding.ActivityBusRouteDetailBinding
import com.san.busing.domain.utils.Utils
import com.san.busing.domain.viewmodelfactory.BusRouteDetailViewModelFactory
import com.san.busing.domain.viewmodelimpl.BusRouteDetailViewModelImpl

class BusRouteDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBusRouteDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusRouteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = BusRouteRepositoryImpl(Utils.getRetrofit(BuildConfig.ROUTES_URL))
        val routeId = intent.getSerializableExtra("routeId") as Id
        val viewModel = ViewModelProvider(this, BusRouteDetailViewModelFactory(repository, routeId)).get(
            BusRouteDetailViewModelImpl::class.java
        )

        initObserver(viewModel)
        initListener(viewModel)
    }

    private fun initObserver(viewModel: BusRouteDetailViewModelImpl) {
        viewModel.routeInfoReady.observe(this, routeInfoReadyObserver(viewModel))
    }

    private fun routeInfoReadyObserver(viewModel: BusRouteDetailViewModelImpl) = Observer<Boolean> {
        if (it) {
            binding.txtRouteName.text = viewModel.routeInfo.name
            binding.txtRouteStartStation.text = viewModel.routeInfo.startStationName
            binding.txtRouteEndStation.text = viewModel.routeInfo.endStationName
        } else {
            binding.txtRouteName.text = EMPTY
            binding.txtRouteStartStation.text = EMPTY
            binding.txtRouteEndStation.text = EMPTY
        }
    }

    private fun initListener(viewModel: BusRouteDetailViewModelImpl) {
        setFabRefreshListener(viewModel)
    }

    private fun setFabRefreshListener(viewModel: BusRouteDetailViewModelImpl) {
        binding.fabRefresh.setOnClickListener { viewModel.load() }
    }

    companion object {
        private const val EMPTY = ""
    }
}
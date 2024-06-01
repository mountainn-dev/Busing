package com.san.busing.view.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.BuildConfig
import com.san.busing.data.repositoryimpl.BusRouteRepositoryImpl
import com.san.busing.data.vo.Id
import com.san.busing.databinding.ActivityBusRouteDetailBinding
import com.san.busing.domain.model.BusStationModel
import com.san.busing.domain.utils.Const
import com.san.busing.domain.utils.Utils
import com.san.busing.domain.viewmodel.BusRouteDetailViewModel
import com.san.busing.domain.viewmodelfactory.BusRouteDetailViewModelFactory
import com.san.busing.domain.viewmodelimpl.BusRouteDetailViewModelImpl
import com.san.busing.view.adapter.BusRouteStationAdapter
import com.san.busing.view.listener.ItemClickEventListener

class BusRouteDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBusRouteDetailBinding
    private lateinit var viewModel: BusRouteDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusRouteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = BusRouteRepositoryImpl(Utils.getRetrofit(BuildConfig.ROUTES_URL), this.applicationContext)
        val routeId = intent.getSerializableExtra(Const.TAG_ROUTE_ID) as Id
        viewModel = ViewModelProvider(this, BusRouteDetailViewModelFactory(repository, routeId)).get(
            BusRouteDetailViewModelImpl::class.java
        )

        initObserver(viewModel)
        initListener(viewModel)
    }

    private fun initObserver(viewModel: BusRouteDetailViewModel) {
        viewModel.routeInfoReady.observe(this, routeInfoReadyObserver(viewModel))
        viewModel.routeStationReady.observe(this, routeStationReadyObserver(viewModel))
    }

    private fun routeInfoReadyObserver(viewModel: BusRouteDetailViewModel) = Observer<Boolean> {
        if (it) { whenRouteInfoReady(viewModel) }
        else { whenRouteInfoNotReady() }
    }

    private fun whenRouteInfoReady(viewModel: BusRouteDetailViewModel) {
        binding.txtRouteName.text = viewModel.routeInfo.name
        binding.txtRouteStartStation.text = viewModel.routeInfo.startStationName
        binding.txtRouteEndStation.text = viewModel.routeInfo.endStationName
    }

    private fun whenRouteInfoNotReady() {
        binding.txtRouteName.text = Const.EMPTY_TEXT
        binding.txtRouteStartStation.text = Const.EMPTY_TEXT
        binding.txtRouteEndStation.text = Const.EMPTY_TEXT
    }

    private fun routeStationReadyObserver(viewModel: BusRouteDetailViewModel) = Observer<Boolean> {
        if (it) { whenRouteStationReady() }
        else { whenRouteStationNotReady() }
    }

    private fun whenRouteStationReady() {
        binding.rvBusRouteStationList.adapter = BusRouteStationAdapter(
            viewModel.routeStation,
            routeStationClickEventListener(viewModel.routeStation)
        )
        binding.rvBusRouteStationList.layoutManager = LinearLayoutManager(this)
        binding.rvBusRouteStationList.visibility = RecyclerView.VISIBLE
    }

    private fun routeStationClickEventListener(items: List<BusStationModel>) = object: ItemClickEventListener {
        override fun onItemClickListener(position: Int) {
            TODO("Not yet implemented")
        }

        override fun onDeleteButtonClickListener(position: Int) {
            TODO("Not yet implemented")
        }
    }

    private fun whenRouteStationNotReady() {
        binding.rvBusRouteStationList.visibility = RecyclerView.INVISIBLE
    }

    private fun initListener(viewModel: BusRouteDetailViewModel) {
        setFabRefreshListener(viewModel)
    }

    private fun setFabRefreshListener(viewModel: BusRouteDetailViewModel) {
        binding.fabRefresh.setOnClickListener { viewModel.loadContent() }
    }
}
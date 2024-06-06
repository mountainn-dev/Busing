package com.san.busing.view.screen

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.BuildConfig
import com.san.busing.R
import com.san.busing.data.repositoryimpl.BusLocationRepositoryImpl
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
import java.util.LinkedList
import java.util.Queue

class BusRouteDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBusRouteDetailBinding
    private lateinit var viewModel: BusRouteDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusRouteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val busRouteRepository = BusRouteRepositoryImpl(Utils.getRetrofit(BuildConfig.ROUTES_URL), this.applicationContext)
        val busLocationRepository = BusLocationRepositoryImpl(Utils.getRetrofit(BuildConfig.LOCATION_URL))
        val routeId = intent.getSerializableExtra(Const.TAG_ROUTE_ID) as Id
        val routeName = intent.getStringExtra(Const.TAG_ROUTE_NAME) ?: Const.EMPTY_TEXT
        viewModel = ViewModelProvider(
            this, BusRouteDetailViewModelFactory(busRouteRepository, busLocationRepository, routeId)
        ).get(BusRouteDetailViewModelImpl::class.java)

        initToolbar(routeName)
        initObserver(viewModel, this)
        initListener(viewModel)
    }

    private fun initToolbar(routeName: String) {
        setTitle(routeName)
    }

    private fun setTitle(routeName: String) {
        binding.txtTitle.text = routeName
        binding.txtRouteName.text = routeName
    }

    private fun initObserver(viewModel: BusRouteDetailViewModel, context: Activity) {
        viewModel.routeInfoReady.observe(
            context as LifecycleOwner,
            routeInfoReadyObserver(viewModel)
        )
        viewModel.routeStationBusReady.observe(
            context as LifecycleOwner,
            routeStationBusReadyObserver(viewModel, context)
        )
    }

    private fun routeInfoReadyObserver(viewModel: BusRouteDetailViewModel) = Observer<Boolean> {
        if (it) { whenRouteInfoReady(viewModel) }
        else { whenRouteInfoNotReady() }
    }

    private fun whenRouteInfoReady(viewModel: BusRouteDetailViewModel) {
        binding.txtRouteStartStation.text = viewModel.routeInfo.startStationName
        binding.txtRouteEndStation.text = viewModel.routeInfo.endStationName
    }

    private fun whenRouteInfoNotReady() {
        binding.txtRouteStartStation.text = Const.EMPTY_TEXT
        binding.txtRouteEndStation.text = Const.EMPTY_TEXT
    }

    private fun routeStationBusReadyObserver(
        viewModel: BusRouteDetailViewModel,
        context: Activity
    ) = Observer<Boolean> {
        if (it) { whenRouteStationAndBusReady(viewModel, context) }
        else { whenRouteStationAndBusNotReady() }
    }

    private fun whenRouteStationAndBusReady(viewModel: BusRouteDetailViewModel, context: Activity) {
        binding.rvBusRouteStationList.adapter = BusRouteStationAdapter(
            viewModel.routeStation,
            LinkedList(viewModel.routeBus),
            routeStationClickEventListener(viewModel.routeStation)
        )
        binding.rvBusRouteStationList.layoutManager = LinearLayoutManager(context)
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

    private fun whenRouteStationAndBusNotReady() {
        binding.rvBusRouteStationList.visibility = RecyclerView.GONE
    }

    private fun initListener(viewModel: BusRouteDetailViewModel) {
        setBtnBackListener()
        setFabRefreshListener(viewModel)
    }

    private fun setBtnBackListener() {
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun setFabRefreshListener(viewModel: BusRouteDetailViewModel) {
        binding.fabRefresh.setOnClickListener { viewModel.load() }
    }
}
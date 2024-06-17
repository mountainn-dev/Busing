package com.san.busing.view.screen

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.san.busing.BuildConfig
import com.san.busing.data.repositoryimpl.BusLocationRepositoryImpl
import com.san.busing.data.repositoryimpl.BusRouteRepositoryImpl
import com.san.busing.data.vo.Id
import com.san.busing.databinding.ActivityBusRouteDetailBinding
import com.san.busing.domain.enums.RouteType
import com.san.busing.domain.model.BusStationModel
import com.san.busing.domain.utils.Const
import com.san.busing.domain.utils.Utils
import com.san.busing.domain.viewmodel.BusRouteDetailViewModel
import com.san.busing.domain.viewmodelfactory.BusRouteDetailViewModelFactory
import com.san.busing.domain.viewmodelimpl.BusRouteDetailViewModelImpl
import com.san.busing.view.adapter.BusRouteStationAdapter
import com.san.busing.view.listener.ItemClickEventListener
import java.util.LinkedList

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
        val routeType = intent.getSerializableExtra(Const.TAG_ROUTE_TYPE) as RouteType
        viewModel = ViewModelProvider(
            this, BusRouteDetailViewModelFactory(busRouteRepository, busLocationRepository, routeId)
        ).get(BusRouteDetailViewModelImpl::class.java)

        initToolbar(routeName, routeType, this)
        initObserver(viewModel, routeType, this)
        initListener(viewModel, routeId)
    }

    private fun initToolbar(routeName: String, routeType: RouteType, context: Activity) {
        setTitle(routeName)
        setBgColor(routeType, context)
    }

    private fun setTitle(routeName: String) {
        binding.txtTitle.text = routeName
        binding.txtRouteName.text = routeName
    }

    private fun setBgColor(type: RouteType, context: Activity) {
        val color = ContextCompat.getColor(context, Utils.getLightColorByRouteType(type))
        binding.ctbRouteDetail.setContentScrimColor(color)
        binding.ctbRouteDetail.setBackgroundColor(color)
    }

    private fun initObserver(
        viewModel: BusRouteDetailViewModel,
        routeType: RouteType,
        context: Activity
    ) {
        viewModel.routeInfoContentReady.observe(
            context as LifecycleOwner,
            routeInfoReadyObserver(viewModel)
        )
        viewModel.routeStationBusContentReady.observe(
            context as LifecycleOwner,
            routeStationBusReadyObserver(viewModel, routeType, context)
        )
    }

    private fun routeInfoReadyObserver(viewModel: BusRouteDetailViewModel) = Observer<Boolean> {
        if (it) { whenRouteInfoReady(viewModel) }
        else { whenRouteInfoNotReady() }
    }

    private fun whenRouteInfoReady(viewModel: BusRouteDetailViewModel) {
        binding.txtRouteStartStation.text = viewModel.routeInfoContent.startStationName
        binding.txtRouteEndStation.text = viewModel.routeInfoContent.endStationName
    }

    private fun whenRouteInfoNotReady() {
        binding.txtRouteStartStation.text = Const.EMPTY_TEXT
        binding.txtRouteEndStation.text = Const.EMPTY_TEXT
    }

    private fun routeStationBusReadyObserver(
        viewModel: BusRouteDetailViewModel,
        routeType: RouteType,
        context: Activity
    ) = Observer<Boolean> {
        if (it) { whenRouteStationAndBusReady(viewModel, routeType, context) }
        else { whenRouteStationAndBusNotReady() }
    }

    private fun whenRouteStationAndBusReady(
        viewModel: BusRouteDetailViewModel, routeType: RouteType, context: Activity) {
        binding.rvBusRouteStationList.adapter = BusRouteStationAdapter(
            routeType,
            viewModel.routeStationContent,
            LinkedList(viewModel.routeBusContent),
            routeStationClickEventListener(viewModel.routeStationContent)
        )
        binding.rvBusRouteStationList.layoutManager = LinearLayoutManager(context)
        binding.txtRouteBusCount.text = String.format(Const.ROUTE_BUS_COUNT, viewModel.routeBusContent.size)
        binding.rvBusRouteStationList.visibility = View.VISIBLE
        binding.pgbBusRouteStation.visibility = View.GONE
    }

    private fun routeStationClickEventListener(items: List<BusStationModel>) = object: ItemClickEventListener {
        override fun onItemClickListener(position: Int) {

        }

        override fun onDeleteButtonClickListener(position: Int) {

        }
    }

    private fun whenRouteStationAndBusNotReady() {
        binding.pgbBusRouteStation.visibility = View.VISIBLE
        binding.rvBusRouteStationList.visibility = View.GONE
    }

    private fun initListener(viewModel: BusRouteDetailViewModel, routeId: Id) {
        setBtnBackListener()
        setFabRefreshListener(viewModel, routeId)
    }

    private fun setBtnBackListener() {
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun setFabRefreshListener(viewModel: BusRouteDetailViewModel, routeId: Id) {
        binding.fabRefresh.setOnClickListener { viewModel.load(routeId) }
    }
}
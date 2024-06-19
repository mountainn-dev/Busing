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
import com.san.busing.data.repositoryimpl.RouteRepositoryImpl
import com.san.busing.data.vo.Id
import com.san.busing.databinding.ActivityBusRouteDetailBinding
import com.san.busing.domain.enums.RouteType
import com.san.busing.domain.model.RouteStationModel
import com.san.busing.domain.utils.Const
import com.san.busing.domain.utils.Utils
import com.san.busing.domain.viewmodel.RouteDetailViewModel
import com.san.busing.domain.viewmodelfactory.RouteDetailViewModelFactory
import com.san.busing.domain.viewmodelimpl.RouteDetailViewModelImpl
import com.san.busing.view.adapter.RouteStationAdapter
import com.san.busing.view.listener.ItemClickEventListener
import java.util.LinkedList

class RouteDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBusRouteDetailBinding
    private lateinit var viewModel: RouteDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusRouteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val busRouteRepository = RouteRepositoryImpl(Utils.getRetrofit(BuildConfig.ROUTES_URL), this.applicationContext)
        val busLocationRepository = BusLocationRepositoryImpl(Utils.getRetrofit(BuildConfig.LOCATION_URL))
        val routeId = intent.getSerializableExtra(Const.TAG_ROUTE_ID) as Id
        val routeName = intent.getStringExtra(Const.TAG_ROUTE_NAME) ?: Const.EMPTY_TEXT
        val routeType = intent.getSerializableExtra(Const.TAG_ROUTE_TYPE) as RouteType
        viewModel = ViewModelProvider(
            this, RouteDetailViewModelFactory(busRouteRepository, busLocationRepository, routeId)
        ).get(RouteDetailViewModelImpl::class.java)

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
        viewModel: RouteDetailViewModel,
        routeType: RouteType,
        context: Activity
    ) {
        viewModel.routeInfoContentReady.observe(
            context as LifecycleOwner,
            routeInfoReadyObserver(viewModel)
        )
        viewModel.routeStationContentReady.observe(
            context as LifecycleOwner,
            routeStationBusReadyObserver(viewModel, routeType, context)
        )
    }

    private fun routeInfoReadyObserver(viewModel: RouteDetailViewModel) = Observer<Boolean> {
        if (it) { whenRouteInfoReady(viewModel) }
        else { whenRouteInfoNotReady() }
    }

    private fun whenRouteInfoReady(viewModel: RouteDetailViewModel) {
        binding.txtRouteStartStation.text = viewModel.routeInfo.startStationName
        binding.txtRouteEndStation.text = viewModel.routeInfo.endStationName
    }

    private fun whenRouteInfoNotReady() {
        binding.txtRouteStartStation.text = Const.EMPTY_TEXT
        binding.txtRouteEndStation.text = Const.EMPTY_TEXT
    }

    private fun routeStationBusReadyObserver(
        viewModel: RouteDetailViewModel,
        routeType: RouteType,
        context: Activity
    ) = Observer<Boolean> {
        if (it) { whenRouteStationAndBusReady(viewModel, routeType, context) }
        else { whenRouteStationAndBusNotReady() }
    }

    private fun whenRouteStationAndBusReady(
        viewModel: RouteDetailViewModel, routeType: RouteType, context: Activity) {
        binding.rvBusRouteStationList.adapter = RouteStationAdapter(
            routeType,
            viewModel.routeStations,
            LinkedList(viewModel.routeBuses),
            routeStationClickEventListener(viewModel.routeStations)
        )
        binding.rvBusRouteStationList.layoutManager = LinearLayoutManager(context)
        binding.txtRouteBusCount.text = String.format(Const.ROUTE_BUS_COUNT, viewModel.routeBuses.size)
        binding.rvBusRouteStationList.visibility = View.VISIBLE
        binding.pgbBusRouteStation.visibility = View.GONE
    }

    private fun routeStationClickEventListener(items: List<RouteStationModel>) = object: ItemClickEventListener {
        override fun onItemClickListener(position: Int) {

        }

        override fun onDeleteButtonClickListener(position: Int) {

        }
    }

    private fun whenRouteStationAndBusNotReady() {
        binding.pgbBusRouteStation.visibility = View.VISIBLE
        binding.rvBusRouteStationList.visibility = View.GONE
    }

    private fun initListener(viewModel: RouteDetailViewModel, routeId: Id) {
        setBtnBackListener()
        setFabRefreshListener(viewModel, routeId)
    }

    private fun setBtnBackListener() {
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun setFabRefreshListener(viewModel: RouteDetailViewModel, routeId: Id) {
        binding.fabRefresh.setOnClickListener { viewModel.load(routeId) }
    }
}
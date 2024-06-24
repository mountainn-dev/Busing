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
import com.san.busing.R
import com.san.busing.data.repositoryimpl.BusLocationRepositoryImpl
import com.san.busing.data.repositoryimpl.RouteRepositoryImpl
import com.san.busing.data.vo.Id
import com.san.busing.databinding.ActivityRouteDetailBinding
import com.san.busing.domain.enums.RouteType
import com.san.busing.domain.model.RouteStationModel
import com.san.busing.domain.utils.Const
import com.san.busing.domain.utils.Utils
import com.san.busing.domain.viewmodel.RouteDetailViewModel
import com.san.busing.domain.viewmodelfactory.RouteDetailViewModelFactory
import com.san.busing.domain.viewmodelimpl.RouteDetailViewModelImpl
import com.san.busing.view.adapter.RouteStationAdapter
import com.san.busing.view.listener.ItemClickEventListener
import com.san.busing.view.widget.ErrorToast

class RouteDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRouteDetailBinding
    private lateinit var viewModel: RouteDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRouteDetailBinding.inflate(layoutInflater)
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
        initListener(viewModel)
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
        viewModel.loadableRemainTime.observe(
            context as LifecycleOwner,
            loadableRemainTimeObserver()
        )
        viewModel.serviceErrorState.observe(
            context as LifecycleOwner,
            serviceErrorStateObserver(viewModel, context)
        )
    }

    private fun routeInfoReadyObserver(viewModel: RouteDetailViewModel) = Observer<Boolean> {
        if (it) { whenRouteInfoReady(viewModel) }
        else { whenRouteInfoNotReady() }
    }

    private fun whenRouteInfoReady(viewModel: RouteDetailViewModel) {
        binding.txtRouteStartStation.text = viewModel.routeInfo.startStationName
        binding.txtRouteEndStation.text = viewModel.routeInfo.endStationName
        binding.btnScrollToStartStation.text = viewModel.routeInfo.startStationName
        binding.btnScrollToEndStation.text = viewModel.routeInfo.endStationName
    }

    private fun whenRouteInfoNotReady() {
        binding.txtRouteStartStation.text = Const.EMPTY_TEXT
        binding.txtRouteEndStation.text = Const.EMPTY_TEXT
        binding.btnScrollToStartStation.text = Const.EMPTY_TEXT
        binding.btnScrollToEndStation.text = Const.EMPTY_TEXT
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
        viewModel: RouteDetailViewModel, routeType: RouteType, context: Activity
    ) {
        val state = binding.rvBusRouteStationList.layoutManager?.onSaveInstanceState()
        binding.rvBusRouteStationList.adapter = RouteStationAdapter(
            routeType,
            viewModel.routeStations,
            viewModel.routeBuses,
            routeStationClickEventListener(viewModel.routeStations),
            context
        )
        binding.rvBusRouteStationList.layoutManager = LinearLayoutManager(context)
        binding.txtRouteBusCount.text = String.format(ROUTE_BUS_COUNT, viewModel.routeBuses.size)
        binding.rvBusRouteStationList.layoutManager?.onRestoreInstanceState(state)
        binding.rvBusRouteStationList.visibility = View.VISIBLE
        binding.pgbBusRouteStation.visibility = View.GONE
        setBtnScrollToEndStation(viewModel)
    }

    private fun routeStationClickEventListener(
        items: List<RouteStationModel>
    ) = object: ItemClickEventListener {
        override fun onItemClickListener(position: Int) {

        }

        override fun onDeleteButtonClickListener(position: Int) {

        }
    }

    private fun setBtnScrollToEndStation(viewModel: RouteDetailViewModel) {
        val idx = turnaroundIndex(viewModel)

        binding.btnScrollToEndStation.setOnClickListener {
            if (binding.abRouteDetail.isLifted)
                binding.rvBusRouteStationList.smoothScrollToPosition(idx + POSITION_VALUE_WHEN_LIFTED)
            else binding.rvBusRouteStationList.smoothScrollToPosition(idx + POSITION_VALUE_WHEN_NOT_LIFTED)
        }
    }

    private fun turnaroundIndex(
        viewModel: RouteDetailViewModel
    ) = viewModel.routeStations.find { it.isTurnaround }?.sequenceNumber ?: 1

    private fun whenRouteStationAndBusNotReady() {
        binding.pgbBusRouteStation.visibility = View.VISIBLE
        binding.rvBusRouteStationList.visibility = View.GONE
    }

    private fun loadableRemainTimeObserver() = Observer<Int> {
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

    private fun serviceErrorStateObserver(
        viewModel: RouteDetailViewModel, context: Activity
    ) = Observer<Boolean> {
        if (it) {
            val toast = ErrorToast(context, viewModel.error)
            if (toast.previousFinished()) toast.show()
        }
    }

    private fun initListener(viewModel: RouteDetailViewModel) {
        setBtnBackListener()
        setBtnScrollToStartStation()
        setFabScrollUp()
        setFabRefreshListener(viewModel)
    }

    private fun setBtnBackListener() {
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun setBtnScrollToStartStation() {
        binding.btnScrollToStartStation.setOnClickListener {
            binding.rvBusRouteStationList.smoothScrollToPosition(Const.ZERO)
        }
    }

    private fun setFabScrollUp() {
        binding.fabScrollUp.setOnClickListener {
            binding.rvBusRouteStationList.scrollToPosition(Const.ZERO)
            binding.abRouteDetail.setExpanded(true)
        }
    }

    private fun setFabRefreshListener(viewModel: RouteDetailViewModel) {
        binding.fabRefresh.setOnClickListener { viewModel.reload() }
    }

    override fun onResume() {
        viewModel.load()
        super.onResume()
    }

    companion object {
        private const val ROUTE_BUS_COUNT = "%d대"
        private const val POSITION_VALUE_WHEN_LIFTED = - 1
        private const val POSITION_VALUE_WHEN_NOT_LIFTED = + 4
    }
}
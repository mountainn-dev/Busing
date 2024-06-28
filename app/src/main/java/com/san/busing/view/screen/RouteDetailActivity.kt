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
import com.san.busing.domain.state.UiState
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
            this, RouteDetailViewModelFactory(
                busRouteRepository, busLocationRepository, routeId, routeName, routeType
            )).get(RouteDetailViewModelImpl::class.java)

        viewModel.update(this)
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
        viewModel.state.observe(
            context as LifecycleOwner,
            uiStateObserver(viewModel, routeType, context)
        )
        viewModel.loadableRemainTime.observe(
            context as LifecycleOwner,
            loadableRemainTimeObserver()
        )
        viewModel.bookMark.observe(
            context as LifecycleOwner,
            bookMarkObserver()
        )
    }

    private fun uiStateObserver(
        viewModel: RouteDetailViewModel,
        routeType: RouteType,
        context: Activity
    ) = Observer<UiState> {
        when (it) {
            UiState.Success -> {
                loadRouteInfo(viewModel)
                loadRouteStation(viewModel, routeType, context)
            }
            UiState.Loading -> {
                unloadRouteInfo()
                loadingView()
            }
            UiState.Timeout -> {
                unloadRouteInfo()
                timeoutView()
            }
            UiState.Error -> {
                unloadRouteInfo()
                errorView(viewModel, context)
            }
        }
    }

    private fun loadRouteInfo(viewModel: RouteDetailViewModel) {
        binding.txtRouteStartStation.text = viewModel.routeInfo.startStationName
        binding.txtRouteEndStation.text = viewModel.routeInfo.endStationName
        binding.btnScrollToStartStation.text = viewModel.routeInfo.startStationName
        binding.btnScrollToEndStation.text = viewModel.routeInfo.endStationName
    }

    private fun loadRouteStation(
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
        binding.llTimeout.visibility = View.GONE
        binding.llServiceError.visibility = View.GONE
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

    private fun unloadRouteInfo() {
        binding.txtRouteStartStation.text = Const.EMPTY_TEXT
        binding.txtRouteEndStation.text = Const.EMPTY_TEXT
        binding.btnScrollToStartStation.text = Const.EMPTY_TEXT
        binding.btnScrollToEndStation.text = Const.EMPTY_TEXT
    }

    private fun loadingView() {
        binding.pgbBusRouteStation.visibility = View.VISIBLE
        binding.rvBusRouteStationList.visibility = View.GONE
        binding.llTimeout.visibility = View.GONE
        binding.llServiceError.visibility = View.GONE
        binding.txtRouteBusCount.text = Const.EMPTY_TEXT
    }

    private fun timeoutView() {
        binding.llTimeout.visibility = View.VISIBLE
        binding.rvBusRouteStationList.visibility = View.GONE
        binding.pgbBusRouteStation.visibility = View.GONE
        binding.llServiceError.visibility = View.GONE
    }

    private fun errorView(viewModel: RouteDetailViewModel, context: Activity) {
        binding.llServiceError.visibility = View.VISIBLE
        binding.rvBusRouteStationList.visibility = View.GONE
        binding.pgbBusRouteStation.visibility = View.GONE
        binding.llTimeout.visibility = View.GONE
        val toast = ErrorToast(context, viewModel.error)
        if (toast.previousFinished()) toast.show()
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

    private fun bookMarkObserver() = Observer<Boolean> {
        if (it) binding.btnBookMark.setImageResource(R.drawable.ic_on_book_mark)
        else binding.btnBookMark.setImageResource(R.drawable.ic_off_book_mark)
    }

    private fun initListener(viewModel: RouteDetailViewModel) {
        setBtnBackListener()
        setBtnBookMarkListener(viewModel)
        setBtnScrollToStartStationListener()
        setBtnRequestListener(viewModel)
        setFabScrollUpListener()
        setFabRefreshListener(viewModel)
    }

    private fun setBtnBackListener() {
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun setBtnBookMarkListener(viewModel: RouteDetailViewModel) {
        binding.btnBookMark.setOnClickListener { viewModel.toggleBookMark() }
    }

    private fun setBtnScrollToStartStationListener() {
        binding.btnScrollToStartStation.setOnClickListener {
            binding.rvBusRouteStationList.smoothScrollToPosition(Const.ZERO)
        }
    }

    private fun setBtnRequestListener(viewModel: RouteDetailViewModel) {
        binding.btnTimeoutRequest.setOnClickListener { viewModel.load() }
        binding.btnServiceErrorRequest.setOnClickListener { viewModel.load() }
    }

    private fun setFabScrollUpListener() {
        binding.fabScrollUp.setOnClickListener {
            binding.rvBusRouteStationList.scrollToPosition(Const.ZERO)
            binding.abRouteDetail.setExpanded(true)
        }
    }

    private fun setFabRefreshListener(viewModel: RouteDetailViewModel) {
        binding.fabRefresh.setOnClickListener { viewModel.reload() }
    }

    /**
     * fun onResume()
     *
     * 액티비티 전환 혹은 포커스가 다시 잡힐 때 호출
     */
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
package com.san.busing.view.screen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import com.san.busing.data.source.remote.retrofit.BusLocationService
import com.san.busing.data.source.remote.retrofit.RouteService
import com.san.busing.data.vo.Id
import com.san.busing.databinding.ActivityRouteDetailBinding
import com.san.busing.domain.enums.RouteType
import com.san.busing.domain.model.RouteStationModel
import com.san.busing.domain.state.UiState
import com.san.busing.domain.utils.Const
import com.san.busing.domain.utils.Utils
import com.san.busing.view.adapter.RouteStationAdapter
import com.san.busing.view.listener.ItemClickEventListener
import com.san.busing.view.viewmodel.RouteDetailViewModel
import com.san.busing.view.viewmodelfactory.RouteDetailViewModelFactory
import com.san.busing.view.viewmodelimpl.RouteDetailViewModelImpl
import com.san.busing.view.widget.ErrorToast

class RouteDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRouteDetailBinding
    private lateinit var viewModel: RouteDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRouteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val busRouteRepository = RouteRepositoryImpl(
            Utils.getRetrofit(BuildConfig.ROUTES_URL).create(RouteService::class.java),
            this.applicationContext
        )
        val busLocationRepository = BusLocationRepositoryImpl(
            Utils.getRetrofit(BuildConfig.LOCATION_URL).create(BusLocationService::class.java)
        )
        val routeId = intent.getSerializableExtra(Const.TAG_ROUTE_ID) as Id
        val routeName = intent.getStringExtra(Const.TAG_ROUTE_NAME) ?: Const.EMPTY_TEXT
        val routeType = intent.getSerializableExtra(Const.TAG_ROUTE_TYPE) as RouteType
        viewModel = ViewModelProvider(
            this, RouteDetailViewModelFactory(
                busRouteRepository, busLocationRepository, routeId, routeName, routeType
            )
        ).get(RouteDetailViewModelImpl::class.java)

        viewModel.updateRecentSearch(this)
        initToolbar(routeName, routeType, this)
        initObserver(routeType, this)
        initListener(this)
    }

    private fun initToolbar(
        routeName: String, routeType: RouteType,
        activity: Activity
    ) {
        setTitle(routeName)
        setBgColor(routeType, activity)
    }

    private fun setTitle(routeName: String) {
        binding.txtTitle.text = routeName
        binding.txtRouteName.text = routeName
    }

    private fun setBgColor(type: RouteType, activity: Activity) {
        val color = ContextCompat.getColor(activity, Utils.getLightColorByRouteType(type))
        binding.ctbRouteDetail.setContentScrimColor(color)
        binding.ctbRouteDetail.setBackgroundColor(color)
    }

    private fun initObserver(routeType: RouteType, activity: Activity) {
        viewModel.state.observe(
            activity as LifecycleOwner,
            uiStateObserver(routeType, activity)
        )
        viewModel.resetTimer.observe(
            activity as LifecycleOwner,
            resetTimerObserver()
        )
        viewModel.bookMark.observe(
            activity as LifecycleOwner,
            bookMarkObserver()
        )
    }

    private fun uiStateObserver(routeType: RouteType, activity: Activity) = Observer<UiState> {
        when (it) {
            UiState.Success -> {
                loadRouteInfo()
                loadRouteStation(routeType, activity)
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
                errorView(activity)
            }
        }
    }

    private fun loadRouteInfo() {
        binding.txtRouteStartStation.text = viewModel.routeInfo.startStationName
        binding.txtRouteEndStation.text = viewModel.routeInfo.endStationName
        binding.btnScrollToStartStation.text = viewModel.routeInfo.startStationName
        binding.btnScrollToEndStation.text = viewModel.routeInfo.endStationName
    }

    private fun loadRouteStation(routeType: RouteType, activity: Activity) {
        val state = binding.rvBusRouteStationList.layoutManager?.onSaveInstanceState()
        binding.rvBusRouteStationList.adapter = RouteStationAdapter(
            routeType,
            viewModel.routeStations,
            viewModel.routeBuses,
            routeStationClickEventListener(viewModel.routeStations),
            activity
        )
        binding.rvBusRouteStationList.layoutManager = LinearLayoutManager(activity)
        binding.txtRouteBusCount.text = String.format(ROUTE_BUS_COUNT, viewModel.routeBuses.size)
        binding.rvBusRouteStationList.layoutManager?.onRestoreInstanceState(state)
        toggleView(binding.rvBusRouteStationList)
        setBtnScrollToEndStation()
    }

    private fun routeStationClickEventListener(
        items: List<RouteStationModel>
    ) = object: ItemClickEventListener {
        override fun onItemClickListener(position: Int) {

        }

        override fun onDeleteButtonClickListener(position: Int) {

        }
    }

    private fun setBtnScrollToEndStation() {
        val idx = turnaroundIndex()

        binding.btnScrollToEndStation.setOnClickListener {
            if (binding.abRouteDetail.isLifted)
                binding.rvBusRouteStationList.smoothScrollToPosition(idx + POSITION_VALUE_WHEN_LIFTED)
            else binding.rvBusRouteStationList.smoothScrollToPosition(idx + POSITION_VALUE_WHEN_NOT_LIFTED)
        }
    }

    private fun turnaroundIndex() = viewModel.routeStations.find { it.isTurnaround }?.sequenceNumber ?: DEFAULT_TURNAROUND_INDEX

    private fun unloadRouteInfo() {
        binding.txtRouteStartStation.text = Const.EMPTY_TEXT
        binding.txtRouteEndStation.text = Const.EMPTY_TEXT
        binding.btnScrollToStartStation.text = Const.EMPTY_TEXT
        binding.btnScrollToEndStation.text = Const.EMPTY_TEXT
    }

    private fun loadingView() {
        toggleView(binding.pgbBusRouteStation)
        binding.txtRouteBusCount.text = Const.EMPTY_TEXT
    }

    private fun timeoutView() {
        toggleView(binding.llTimeout)
    }

    private fun errorView(activity: Activity) {
        toggleView(binding.llServiceError)
        val toast = ErrorToast(activity, viewModel.error)
        if (toast.previousFinished()) toast.show()
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
        setBtnRouteInfoListener(activity)
        setBtnBookMarkListener(activity)
        setBtnScrollToStartStationListener()
        setBtnRequestListener()
        setFabScrollUpListener()
        setFabRefreshListener()
    }

    private fun setBtnBackListener() {
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun setBtnRouteInfoListener(activity: Activity) {
        binding.btnRouteInfo.setOnClickListener {
            if (routeInfoReady()) sendUserToRouteInfoScreen(activity)
        }
    }

    private fun routeInfoReady() = viewModel.state.value == UiState.Success

    private fun sendUserToRouteInfoScreen(activity: Activity) {
        val intent = Intent(activity, RouteInfoActivity::class.java)
        intent.putExtra(Const.TAG_ROUTE_INFO, viewModel.routeInfo)

        startActivity(intent)
    }

    private fun setBtnBookMarkListener(activity: Activity) {
        binding.btnBookMark.setOnClickListener {
            if (viewModel.bookMark.value!!) Toast.makeText(activity, BOOKMARK_UNREGISTER_MESSAGE, Toast.LENGTH_SHORT).show()
            else Toast.makeText(activity, BOOKMARK_REGISTER_MESSAGE, Toast.LENGTH_SHORT).show()
            viewModel.toggleBookMark()
        }
    }

    private fun setBtnScrollToStartStationListener() {
        binding.btnScrollToStartStation.setOnClickListener {
            binding.rvBusRouteStationList.smoothScrollToPosition(Const.ZERO)
        }
    }

    private fun setBtnRequestListener() {
        binding.btnTimeoutRequest.setOnClickListener { viewModel.load() }
        binding.btnServiceErrorRequest.setOnClickListener { viewModel.load() }
    }

    private fun setFabScrollUpListener() {
        binding.fabScrollUp.setOnClickListener {
            binding.rvBusRouteStationList.scrollToPosition(Const.ZERO)
            binding.abRouteDetail.setExpanded(true)
        }
    }

    private fun setFabRefreshListener() {
        binding.fabRefresh.setOnClickListener { viewModel.loadWithTimer() }
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

    private fun toggleView(view: View) {
        binding.pgbBusRouteStation.visibility = if (view == binding.pgbBusRouteStation) View.VISIBLE else View.GONE
        binding.rvBusRouteStationList.visibility = if (view == binding.rvBusRouteStationList) View.VISIBLE else View.GONE
        binding.llTimeout.visibility = if (view == binding.llTimeout) View.VISIBLE else View.GONE
        binding.llServiceError.visibility = if (view == binding.llServiceError) View.VISIBLE else View.GONE
    }

    companion object {
        private const val ROUTE_BUS_COUNT = "%d대"
        private const val POSITION_VALUE_WHEN_LIFTED = - 1
        private const val POSITION_VALUE_WHEN_NOT_LIFTED = + 4
        private const val DEFAULT_TURNAROUND_INDEX = 1

        private const val BOOKMARK_REGISTER_MESSAGE = "즐겨찾기가 등록되었습니다."
        private const val BOOKMARK_UNREGISTER_MESSAGE = "즐겨찾기가 해제되었습니다."
    }
}
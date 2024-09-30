package com.san.busing.view.screen.route

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.san.busing.R
import com.san.busing.data.repositoryimpl.route.RouteRepositoryImpl
import com.san.busing.data.source.local.provider.RoomDBProvider
import com.san.busing.data.source.remote.retrofit.provider.RetrofitProvider
import com.san.busing.databinding.ActivityRouteDetailBinding
import com.san.busing.domain.enums.RouteType
import com.san.busing.domain.model.route.RouteModel
import com.san.busing.domain.model.station.StationModel
import com.san.busing.domain.modelimpl.station.StationModels
import com.san.busing.domain.state.UiState
import com.san.busing.domain.utils.Const
import com.san.busing.domain.utils.Utils
import com.san.busing.view.adapter.route.RouteStationAdapter
import com.san.busing.view.listener.ItemClickEventListener
import com.san.busing.view.listener.RecyclerViewScrollListener
import com.san.busing.view.screen.station.StationDetailActivity
import com.san.busing.view.viewmodel.route.RouteDetailViewModel
import com.san.busing.view.viewmodelfactory.route.RouteDetailViewModelFactory
import com.san.busing.view.viewmodelimpl.route.RouteDetailViewModelImpl
import com.san.busing.view.widget.ErrorToast

class RouteDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRouteDetailBinding
    private lateinit var viewModel: RouteDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRouteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val busRouteRepository = RouteRepositoryImpl(
            RetrofitProvider.getRouteService(),
            RetrofitProvider.getBusLocationService(),
            RoomDBProvider.get(applicationContext)
        )
        val route = intent.getSerializableExtra(Const.TAG_ROUTE) as RouteModel
        viewModel = ViewModelProvider(
            this, RouteDetailViewModelFactory(
                busRouteRepository, route
            )
        ).get(RouteDetailViewModelImpl::class.java)

        viewModel.updateRecentSearch(this)
        initToolbar(route, this)
        initObserver(route, this)
        initListener(this)
    }

    private fun initToolbar(route: RouteModel, activity: Activity) {
        setTitle(route.name)
        setBgColor(route.type, activity)
        startEllipsizeMarqueeEffect()
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

    private fun startEllipsizeMarqueeEffect() {
        binding.txtTitle.setHorizontallyScrolling(true)
        binding.txtTitle.isSelected = true
    }

    private fun initObserver(route: RouteModel, activity: Activity) {
        viewModel.state.observe(
            activity as LifecycleOwner,
            uiStateObserver(route.type, activity)
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
                if (viewModel.viaStations.isEmpty()) noViaStationView()
                else loadViaStation(routeType, activity)
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

    private fun noViaStationView() {
        toggleView(binding.txtNoViaStations)
    }

    private fun loadViaStation(routeType: RouteType, activity: Activity) {
        val scrollState = binding.rvBusRouteStationList.layoutManager?.onSaveInstanceState()
        binding.rvBusRouteStationList.adapter = RouteStationAdapter(
            routeType,
            viewModel.viaStations,
            viewModel.buses,
            routeStationClickEventListener(viewModel.viaStations, activity)
        )
        binding.rvBusRouteStationList.layoutManager = LinearLayoutManager(activity)
        viewModel.keywordMatchingStationIndex.observe(
            activity as LifecycleOwner,
            keywordMatchingStationIndexObserver()
        )
        binding.txtRouteBusCount.text = String.format(ROUTE_BUS_COUNT, viewModel.buses.count())
        binding.rvBusRouteStationList.layoutManager?.onRestoreInstanceState(scrollState)
        toggleView(binding.rvBusRouteStationList)
        setBtnScrollToEndStation()
    }

    private fun routeStationClickEventListener(
        items: StationModels,
        activity: Activity
    ) = object: ItemClickEventListener {
        override fun onItemClickListener(position: Int) {
            sendUserToStationDetailScreen(items.get(position), activity)
        }

        override fun onDeleteButtonClickListener(position: Int) {}
    }

    private fun sendUserToStationDetailScreen(item: StationModel, activity: Activity) {
        val intent = Intent(activity, StationDetailActivity::class.java)
        intent.putExtra(Const.TAG_STATION, item)

        startActivity(intent)
    }

    private fun setBtnScrollToEndStation() {
        val idx = viewModel.viaStations.turnaroundSequence()

        binding.btnScrollToEndStation.setOnClickListener {
            (binding.rvBusRouteStationList.layoutManager as LinearLayoutManager)
                .scrollToPositionWithOffset(idx, 0)
        }
    }

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
            binding.fabRefresh.isClickable = true
            binding.fabTime.visibility = View.GONE
        } else {
            if (binding.fabTime.visibility == View.GONE) {
                binding.fabTime.visibility = View.VISIBLE
                binding.fabRefresh.isClickable = false
                binding.fabRefresh.setImageResource(android.R.color.transparent)
            }
            binding.fabTime.text = it.toString()
        }
    }

    private fun bookMarkObserver() = Observer<Boolean> {
        if (it) binding.btnBookMark.setImageResource(R.drawable.ic_on_book_mark)
        else binding.btnBookMark.setImageResource(R.drawable.ic_off_book_mark)
    }

    private fun keywordMatchingStationIndexObserver() = Observer<Int> {
        (binding.rvBusRouteStationList.layoutManager as LinearLayoutManager)
            .scrollToPositionWithOffset(it, 0)
    }

    private fun initListener(activity: Activity) {
        setBtnBackListener()
        setBtnRouteInfoListener(activity)
        setBtnBookMarkListener(activity)
        setEdViaStationListener()
        setBtnDeleteKeywordListener(activity)
        setBtnMoveMatchingStationListener()
        setBtnScrollToStartStationListener()
        setBtnRequestListener()
        setFabScrollUpListener()
        setFabRefreshListener()
        setRvViaStationScrollListener(activity)
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
            if (viewModel.bookMark.value!!) Toast.makeText(
                activity,
                BOOKMARK_UNREGISTER_MESSAGE,
                Toast.LENGTH_SHORT
            ).show()
            else Toast.makeText(activity, BOOKMARK_REGISTER_MESSAGE, Toast.LENGTH_SHORT).show()
            viewModel.toggleBookMark()
        }
    }

    private fun setEdViaStationListener() {
        binding.edViaStation.doAfterTextChanged {
            viewModel.find(it.toString())
        }
    }

    private fun setBtnDeleteKeywordListener(activity: Activity) {
        binding.btnDeleteKeyword.setOnClickListener {
            viewModel.clearKeyword()
            binding.edViaStation.setText(viewModel.keyword)
            showSoftInput(binding.edViaStation, activity)
        }
    }

    private fun showSoftInput(view: View, activity: Activity) {
        if (view.requestFocus()) {
            val imm = activity.getSystemService(InputMethodManager::class.java)
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun setBtnMoveMatchingStationListener() {
        binding.btnMoveUpMatchingStation.setOnClickListener { viewModel.moveUpMatchingStation() }
        binding.btnMoveDownMatchingStation.setOnClickListener { viewModel.moveDownMatchingStation() }
    }

    private fun setBtnScrollToStartStationListener() {
        binding.btnScrollToStartStation.setOnClickListener {
            binding.rvBusRouteStationList.scrollToPosition(Const.ZERO)
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

    private fun setRvViaStationScrollListener(activity: Activity) {
        binding.rvBusRouteStationList.addOnScrollListener(RecyclerViewScrollListener(activity))
    }

    override fun onResume() {
        viewModel.load()
        super.onResume()
    }

    private fun toggleView(view: View) {
        binding.pgbBusRouteStation.visibility = visibleWhenTrue(view == binding.pgbBusRouteStation)
        binding.rvBusRouteStationList.visibility = visibleWhenTrue(view == binding.rvBusRouteStationList)
        binding.txtNoViaStations.visibility = visibleWhenTrue(view == binding.txtNoViaStations)
        binding.llTimeout.visibility = visibleWhenTrue(view == binding.llTimeout)
        binding.llServiceError.visibility = visibleWhenTrue(view == binding.llServiceError)
    }

    private fun visibleWhenTrue(state: Boolean) = if (state) View.VISIBLE else View.GONE

    companion object {
        private const val ROUTE_BUS_COUNT = "%d대"
        private const val BOOKMARK_REGISTER_MESSAGE = "즐겨찾기가 등록되었습니다."
        private const val BOOKMARK_UNREGISTER_MESSAGE = "즐겨찾기가 해제되었습니다."
    }
}
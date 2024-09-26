package com.san.busing.view.screen.station

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.san.busing.BuildConfig
import com.san.busing.R
import com.san.busing.data.repositoryimpl.route.RouteRepositoryImpl
import com.san.busing.data.repositoryimpl.station.StationRepositoryImpl
import com.san.busing.data.source.remote.retrofit.route.BusLocationService
import com.san.busing.data.source.remote.retrofit.route.RouteService
import com.san.busing.data.source.remote.retrofit.station.BusArrivalService
import com.san.busing.data.source.remote.retrofit.station.StationService
import com.san.busing.databinding.ActivityStationDetailBinding
import com.san.busing.domain.model.route.RouteModel
import com.san.busing.domain.model.station.StationModel
import com.san.busing.domain.modelimpl.route.RouteModels
import com.san.busing.domain.state.UiState
import com.san.busing.domain.utils.Const
import com.san.busing.domain.utils.Utils
import com.san.busing.view.adapter.station.StationBusArrivalAdapter
import com.san.busing.view.listener.ItemClickEventListener
import com.san.busing.view.screen.route.RouteDetailActivity
import com.san.busing.view.viewmodel.station.StationDetailViewModel
import com.san.busing.view.viewmodelfactory.station.StationDetailViewModelFactory
import com.san.busing.view.viewmodelimpl.station.StationDetailViewModelImpl
import com.san.busing.view.widget.ErrorToast

class StationDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStationDetailBinding
    private lateinit var viewModel: StationDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val stationRepository = StationRepositoryImpl(
            Utils.getRetrofit(BuildConfig.STATION_URL).create(StationService::class.java),
            Utils.getRetrofit(BuildConfig.ARRIVAL_URL).create(BusArrivalService::class.java),
            this.applicationContext
        )
        val routeRepository = RouteRepositoryImpl(
            Utils.getRetrofit(BuildConfig.ROUTES_URL).create(RouteService::class.java),
            Utils.getRetrofit(BuildConfig.LOCATION_URL).create(BusLocationService::class.java),
            this.applicationContext
        )
        val station = intent.getSerializableExtra(Const.TAG_STATION) as StationModel
        viewModel = ViewModelProvider(
            this, StationDetailViewModelFactory(stationRepository, routeRepository, station)
        ).get(StationDetailViewModelImpl::class.java)

        viewModel.updateRecentSearch(this)
        initAnimEffect()
        initToolbar(station)
        initObserver(this)
        initListener(this)
    }

    private fun initAnimEffect() {
        initEllipsizeMarqueeEffect()
    }

    private fun initEllipsizeMarqueeEffect() {
        binding.txtTitle.setHorizontallyScrolling(true)
        binding.txtTitle.isSelected = true
    }

    private fun initToolbar(station: StationModel) {
        setTitle(station.name)
        setContent(station)
    }

    private fun setTitle(stationName: String) {
        binding.txtTitle.text = stationName
    }

    private fun setContent(station: StationModel) {
        binding.txtStationName.text = station.name
        binding.txtStationMobileNo.text = station.mobileNo
        binding.txtRegionName.text = station.regionName
    }

    private fun initObserver(activity: Activity) {
        viewModel.state.observe(
            activity as LifecycleOwner,
            stateObserver(activity)
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

    private fun stateObserver(activity: Activity) = Observer<UiState> {
        when (it) {
            UiState.Success -> {
                loadStationViaRoutes(activity)
            }
            UiState.Loading -> {
                loadingView()
            }
            UiState.Timeout -> {
                timeoutView()
            }
            UiState.Error -> {
                errorView(activity)
            }
        }
    }

    private fun loadStationViaRoutes(activity: Activity) {
        val scrollState = binding.rvStationViaRouteList.layoutManager?.onSaveInstanceState()
        binding.rvStationViaRouteList.adapter = StationBusArrivalAdapter(
            viewModel.viaRoutes,
            viewModel.nextStations,
            viewModel.busArrivals,
            stationBusArrivalClickEventListener(viewModel.viaRoutes, activity),
            activity
        )
        binding.rvStationViaRouteList.layoutManager = LinearLayoutManager(activity)
        binding.rvStationViaRouteList.layoutManager?.onRestoreInstanceState(scrollState)
        toggleView(binding.rvStationViaRouteList)
    }

    private fun stationBusArrivalClickEventListener(
        items: RouteModels,
        activity: Activity
    ) = object : ItemClickEventListener {
        override fun onItemClickListener(position: Int) {
            sendUserToRouteDetailScreen(items.get(position), activity)
        }

        override fun onDeleteButtonClickListener(position: Int) {}
    }

    private fun sendUserToRouteDetailScreen(item: RouteModel, activity: Activity) {
        val intent = Intent(activity, RouteDetailActivity::class.java)
        intent.putExtra(Const.TAG_ROUTE, item)

        startActivity(intent)
    }

    private fun loadingView() {
        toggleView(binding.pgbStationDetail)
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

    private fun initListener(activity: Activity) {
        setBtnBackListener()
        setBtnBookMarkListener(activity)
        setBtnRequestListener()
        setFabScrollUpListener()
        setFabRefreshListener()
    }

    private fun setBtnBackListener() {
        binding.btnBack.setOnClickListener { finish() }
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

    private fun setBtnRequestListener() {
        binding.btnTimeoutRequest.setOnClickListener { viewModel.load() }
        binding.btnServiceErrorRequest.setOnClickListener { viewModel.load() }
    }

    private fun setFabScrollUpListener() {
        binding.fabScrollUp.setOnClickListener {
            binding.rvStationViaRouteList.scrollToPosition(Const.ZERO)
            binding.abStationDetail.setExpanded(true)
        }
    }

    private fun setFabRefreshListener() {
        binding.fabRefresh.setOnClickListener { viewModel.loadWithTimer() }
    }

    private fun toggleView(view: View) {
        binding.pgbStationDetail.visibility = if (view == binding.pgbStationDetail) View.VISIBLE else View.GONE
        binding.rvStationViaRouteList.visibility = if (view == binding.rvStationViaRouteList) View.VISIBLE else View.GONE
        binding.llTimeout.visibility = if (view == binding.llTimeout) View.VISIBLE else View.GONE
        binding.llServiceError.visibility = if (view == binding.llServiceError) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        viewModel.load()
    }

    companion object {
        private const val BOOKMARK_REGISTER_MESSAGE = "즐겨찾기가 등록되었습니다."
        private const val BOOKMARK_UNREGISTER_MESSAGE = "즐겨찾기가 해제되었습니다."
    }
}
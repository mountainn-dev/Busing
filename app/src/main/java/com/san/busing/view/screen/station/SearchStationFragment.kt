package com.san.busing.view.screen.station

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.san.busing.data.repositoryimpl.station.StationRepositoryImpl
import com.san.busing.data.source.local.provider.RoomDBProvider
import com.san.busing.data.source.remote.retrofit.provider.RetrofitProvider
import com.san.busing.databinding.FragmentSearchStationBinding
import com.san.busing.domain.model.station.StationModel
import com.san.busing.domain.modelimpl.station.StationModels
import com.san.busing.domain.modelimpl.station.StationRecentSearchModels
import com.san.busing.domain.state.UiState
import com.san.busing.domain.utils.Const
import com.san.busing.view.adapter.station.StationRecentSearchAdapter
import com.san.busing.view.adapter.station.StationSearchResultAdapter
import com.san.busing.view.listener.ItemClickEventListener
import com.san.busing.view.listener.RecyclerViewScrollListener
import com.san.busing.view.viewmodel.station.SearchStationViewModel
import com.san.busing.view.viewmodelfactory.station.SearchStationViewModelFactory
import com.san.busing.view.viewmodelimpl.station.SearchStationViewModelImpl
import com.san.busing.view.widget.ErrorToast

class SearchStationFragment : Fragment() {
    private lateinit var binding: FragmentSearchStationBinding
    private lateinit var viewModel: SearchStationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository =
            StationRepositoryImpl(
                RetrofitProvider.getStationService(),
                RetrofitProvider.getBusArrivalService(),
                RoomDBProvider.get(requireActivity().applicationContext),
            )
        viewModel =
            ViewModelProvider(requireActivity(), SearchStationViewModelFactory(repository)).get(
                SearchStationViewModelImpl::class.java,
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchStationBinding.inflate(layoutInflater)

        initObserver(requireActivity())
        initListener(requireActivity())

        return binding.root
    }

    private fun initObserver(activity: Activity) {
        viewModel.state.observe(
            activity as LifecycleOwner,
            stateObserver(activity),
        )
        viewModel.recentSearchContentReady.observe(
            viewLifecycleOwner,
            recentSearchContentReadyObserver(activity),
        )
    }

    private fun stateObserver(activity: Activity) =
        Observer<UiState> {
            when (it) {
                UiState.Success -> {
                    if (viewModel.stations.isEmpty()) {
                        noSearchResultView()
                    } else {
                        loadSearchResult(activity)
                    }
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

    private fun noSearchResultView() {
        toggleView(binding.txtNoResult)
    }

    private fun loadSearchResult(activity: Activity) {
        binding.rvSearchResult.adapter =
            StationSearchResultAdapter(
                viewModel.stations,
                searchResultItemClickEventListener(viewModel.stations, activity),
            )
        binding.rvSearchResult.layoutManager = LinearLayoutManager(context)
        toggleView(binding.rvSearchResult)
    }

    private fun searchResultItemClickEventListener(
        items: StationModels,
        activity: Activity,
    ) = object : ItemClickEventListener {
        override fun onItemClickListener(position: Int) {
            sendUserToStationDetailScreen(activity, items.get(position))
        }

        override fun onDeleteButtonClickListener(position: Int) {}
    }

    private fun sendUserToStationDetailScreen(
        activity: Activity,
        station: StationModel,
    ) {
        val intent = Intent(activity, StationDetailActivity::class.java)
        intent.putExtra(Const.TAG_STATION, station)

        startActivity(intent)
    }

    private fun loadingView() {
        toggleView(binding.pgbSearchRoute)
    }

    private fun timeoutView() {
        toggleView(binding.llTimeout)
    }

    private fun errorView(activity: Activity) {
        toggleView(binding.llServiceError)
        val toast = ErrorToast(activity, viewModel.error)
        if (toast.previousFinished()) toast.show()
    }

    private fun recentSearchContentReadyObserver(activity: Activity) =
        Observer<Boolean> {
            if (it) {
                whenRecentSearchReady(activity)
            } else {
                whenRecentSearchNotReady()
            }
        }

    private fun whenRecentSearchReady(activity: Activity) {
        binding.rvRecentSearch.adapter =
            StationRecentSearchAdapter(
                viewModel.stationRecentSearches,
                recentSearchItemClickEventListener(viewModel.stationRecentSearches, activity),
                activity,
            )
        binding.rvRecentSearch.layoutManager =
            LinearLayoutManager(
                activity, LinearLayoutManager.HORIZONTAL, false,
            )
        binding.rvRecentSearch.visibility = View.VISIBLE
    }

    private fun whenRecentSearchNotReady() {
        binding.rvRecentSearch.visibility = View.GONE
    }

    private fun recentSearchItemClickEventListener(
        items: StationRecentSearchModels,
        activity: Activity,
    ) = object : ItemClickEventListener {
        override fun onItemClickListener(position: Int) {
            sendUserToStationDetailScreen(activity, items.get(position))
        }

        override fun onDeleteButtonClickListener(position: Int) {
            viewModel.deleteRecentSearch(position)
        }
    }

    private fun initListener(activity: Activity) {
        setEdRouteListener()
        setBtnDeleteSearchKeywordListener(activity)
        setBtnDeleteAllRecentSearchListener(activity)
        setRvBusRouteScrollListener(activity)
        setBtnRequestListener()
    }

    private fun setEdRouteListener() {
        binding.edStation.doAfterTextChanged { text ->
            viewModel.search(text.toString())
        }
    }

    private fun setBtnDeleteSearchKeywordListener(activity: Activity) {
        binding.btnDeleteSearchKeyword.setOnClickListener {
            viewModel.clearKeyword()
            binding.edStation.setText(viewModel.keyword)
            showSoftInput(binding.edStation, activity)
        }
    }

    private fun setBtnDeleteAllRecentSearchListener(activity: Activity) {
        binding.btnDeleteAllRecentSearch.setOnClickListener {
            viewModel.deleteAllRecentSearches(activity)
        }
    }

    private fun showSoftInput(
        view: View,
        activity: Activity,
    ) {
        if (view.requestFocus()) {
            val imm = activity.getSystemService(InputMethodManager::class.java)
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun setRvBusRouteScrollListener(context: Activity) {
        binding.rvSearchResult.addOnScrollListener(RecyclerViewScrollListener(context))
    }

    private fun setBtnRequestListener() {
        binding.btnTimeoutRequest.setOnClickListener {
            viewModel.search(viewModel.keyword)
        }
        binding.btnServiceErrorRequest.setOnClickListener {
            viewModel.search(viewModel.keyword)
        }
    }

    override fun onStart() {
        super.onStart()
        restore()
    }

    private fun restore() {
        viewModel.restore()
    }

    private fun toggleView(view: View) {
        binding.pgbSearchRoute.visibility = visibleWhenTrue(view == binding.pgbSearchRoute)
        binding.rvSearchResult.visibility = visibleWhenTrue(view == binding.rvSearchResult)
        binding.txtNoResult.visibility = visibleWhenTrue(view == binding.txtNoResult)
        binding.llTimeout.visibility = visibleWhenTrue(view == binding.llTimeout)
        binding.llServiceError.visibility = visibleWhenTrue(view == binding.llServiceError)
    }

    private fun visibleWhenTrue(state: Boolean) = if (state) View.VISIBLE else View.GONE
}

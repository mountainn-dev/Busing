package com.san.busing.view.screen

import android.app.Activity
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
import com.san.busing.BuildConfig
import com.san.busing.data.repositoryimpl.StationRepositoryImpl
import com.san.busing.data.source.remote.retrofit.StationService
import com.san.busing.databinding.FragmentSearchStationBinding
import com.san.busing.domain.model.StationSummaryModel
import com.san.busing.domain.state.UiState
import com.san.busing.domain.utils.Utils
import com.san.busing.view.adapter.StationSearchResultAdapter
import com.san.busing.view.listener.ItemClickEventListener
import com.san.busing.view.listener.RecyclerViewScrollListener
import com.san.busing.view.viewmodel.SearchStationViewModel
import com.san.busing.view.viewmodelfactory.SearchStationViewModelFactory
import com.san.busing.view.viewmodelimpl.SearchStationViewModelImpl

class SearchStationFragment : Fragment() {
    private lateinit var binding: FragmentSearchStationBinding
    private lateinit var viewModel: SearchStationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = StationRepositoryImpl(
            Utils.getRetrofit(BuildConfig.STATION_URL).create(StationService::class.java)
        )
        viewModel = ViewModelProvider(requireActivity(), SearchStationViewModelFactory(repository)).get(
            SearchStationViewModelImpl::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchStationBinding.inflate(layoutInflater)

        initObserver(requireActivity())
        initListener(requireActivity())

        return binding.root
    }

    private fun initObserver(activity: Activity) {
        viewModel.state.observe(
            activity as LifecycleOwner,
            stateObserver(activity)
        )
    }

    private fun stateObserver(activity: Activity) = Observer<UiState> {
        when (it) {
            UiState.Success -> {
                if (viewModel.stationSummaries.isEmpty()) noSearchResultView()
                else loadSearchResult(activity)
            }
            UiState.Loading -> {
                loadingView()
            }
            UiState.Timeout -> {
                timeoutView()
            }
            UiState.Error -> {
                errorView()
            }
        }
    }

    private fun noSearchResultView() {
        toggleView(binding.txtNoResult)
    }

    private fun loadSearchResult(activity: Activity) {
        binding.rvSearchResult.adapter = StationSearchResultAdapter(
            viewModel.stationSummaries,
            searchResultItemClickEventListener(viewModel.stationSummaries, activity),
        )
        binding.rvSearchResult.layoutManager = LinearLayoutManager(context)
        toggleView(binding.rvSearchResult)
    }

    private fun searchResultItemClickEventListener(
        items: List<StationSummaryModel>,
        activity: Activity
    ) = object : ItemClickEventListener {
        override fun onItemClickListener(position: Int) {
            // sendUserToStationDetailScreen
        }

        override fun onDeleteButtonClickListener(position: Int) {}
    }

    private fun loadingView() {
        toggleView(binding.pgbSearchRoute)
    }

    private fun timeoutView() {
        toggleView(binding.llTimeout)
    }

    private fun errorView() {
        toggleView(binding.llServiceError)
    }

    private fun initListener(activity: Activity) {
        setEdRouteListener()
        setBtnDeleteSearchKeywordListener(activity)
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

    private fun showSoftInput(view: View, activity: Activity) {
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

    private fun toggleView(view: View) {
        binding.rvSearchResult.visibility = if (view == binding.rvSearchResult) View.VISIBLE else View.GONE
        binding.txtNoResult.visibility = if (view == binding.txtNoResult) View.VISIBLE else View.GONE
        binding.pgbSearchRoute.visibility = if (view == binding.pgbSearchRoute) View.VISIBLE else View.GONE
        binding.llTimeout.visibility = if (view == binding.llTimeout) View.VISIBLE else View.GONE
        binding.llServiceError.visibility = if (view == binding.llServiceError) View.VISIBLE else View.GONE
    }
}
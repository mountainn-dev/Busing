package com.san.busing.view.screen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.san.busing.BuildConfig
import com.san.busing.data.repositoryimpl.RouteRepositoryImpl
import com.san.busing.data.source.remote.retrofit.BusLocationService
import com.san.busing.data.source.remote.retrofit.RouteService
import com.san.busing.databinding.FragmentSearchRouteBinding
import com.san.busing.domain.model.RouteModel
import com.san.busing.domain.modelimpl.RouteModels
import com.san.busing.domain.modelimpl.RouteRecentSearchModels
import com.san.busing.domain.state.UiState
import com.san.busing.domain.utils.Const
import com.san.busing.domain.utils.Utils
import com.san.busing.view.adapter.RouteRecentSearchAdapter
import com.san.busing.view.adapter.RouteSearchResultAdapter
import com.san.busing.view.listener.ItemClickEventListener
import com.san.busing.view.listener.RecyclerViewScrollListener
import com.san.busing.view.viewmodel.SearchRouteViewModel
import com.san.busing.view.viewmodelfactory.SearchRouteViewModelFactory
import com.san.busing.view.viewmodelimpl.SearchRouteViewModelImpl
import com.san.busing.view.widget.ErrorToast

class SearchRouteFragment : Fragment() {
    private lateinit var binding: FragmentSearchRouteBinding
    private lateinit var viewModel: SearchRouteViewModel

    /**
     * override fun onCreate(): void
     *
     * 프레그먼트 탭 전환 시 onCreateView() 직전에 호출
     * 레포지토리 및 뷰모델 초기화 등 뷰 이전에 처리될 작업은 onCreate() 에서 진행
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = RouteRepositoryImpl(
            Utils.getRetrofit(BuildConfig.ROUTES_URL).create(RouteService::class.java),
            Utils.getRetrofit(BuildConfig.LOCATION_URL).create(BusLocationService::class.java),
            requireActivity().applicationContext
        )
        viewModel = ViewModelProvider(requireActivity(), SearchRouteViewModelFactory(repository)).get(
            SearchRouteViewModelImpl::class.java
        )
    }

    /**
     * override fun onCreateView(): View
     *
     * 프레그먼트 생성 및 bottomNav 탭 전환 시 호출
     * 뷰 관련 작업 실행
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchRouteBinding.inflate(layoutInflater)

        initObserver(requireActivity())
        initListener(requireActivity())

        return binding.root
    }

    private fun initObserver(context: Activity) {
        viewModel.state.observe(
            viewLifecycleOwner,
            uiStateObserver(context)
        )
        viewModel.recentSearchContentReady.observe(
            viewLifecycleOwner,
            recentSearchContentReadyObserver(context)
        )
    }

    private fun uiStateObserver(activity: Activity) = Observer<UiState> {
        when (it) {
            UiState.Success -> {
                if (viewModel.routeSummaries.isEmpty()) noSearchResultView()
                else loadSearchResult(activity)
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

    private fun loadSearchResult(context: Activity) {
        binding.rvSearchResult.adapter = RouteSearchResultAdapter(
            viewModel.routeSummaries,
            searchResultItemClickEventListener(viewModel.routeSummaries, context),
            context
        )
        binding.rvSearchResult.layoutManager = LinearLayoutManager(context)
        toggleView(binding.rvSearchResult)
    }

    private fun searchResultItemClickEventListener(
        items: RouteModels,
        activity: Activity
    ) = object : ItemClickEventListener {
        override fun onItemClickListener(position: Int) {
            sendUserToRouteDetailScreen(activity, items.get(position))
        }

        override fun onDeleteButtonClickListener(position: Int) {}
    }

    private fun sendUserToRouteDetailScreen(activity: Activity, route: RouteModel) {
        val intent = Intent(activity, RouteDetailActivity::class.java)
        intent.putExtra(Const.TAG_ROUTE, route)

        activity.startActivity(intent)
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

    private fun recentSearchContentReadyObserver(context: Activity) = Observer<Boolean> {
        if (it) { whenRecentSearchReady(context) }
        else { whenRecentSearchNotReady() }
    }

    private fun whenRecentSearchReady(context: Activity) {
        binding.rvRecentSearch.adapter = RouteRecentSearchAdapter(
            viewModel.routeRecentSearches,
            recentSearchItemClickEventListener(viewModel.routeRecentSearches, context),
            context
        )
        binding.rvRecentSearch.layoutManager = LinearLayoutManager(
            activity, LinearLayoutManager.HORIZONTAL, false
        )
        binding.rvRecentSearch.visibility = View.VISIBLE
    }

    private fun whenRecentSearchNotReady() {
        binding.rvRecentSearch.visibility = View.GONE
    }

    private fun recentSearchItemClickEventListener(
        items: RouteRecentSearchModels,
        activity: Activity
    ) = object : ItemClickEventListener {
        override fun onItemClickListener(position: Int) {
            sendUserToRouteDetailScreen(activity, items.get(position))
        }

        override fun onDeleteButtonClickListener(position: Int) {
            viewModel.deleteRecentSearch(position)
        }
    }

    private fun initListener(context: Activity) {
        setEdRouteListener()
        setBtnDeleteSearchKeywordListener(context)
        setBtnDeleteAllRecentSearchListener(context)
        setRvBusRouteScrollListener(context)
        setBtnRequestListener()
    }

    private fun setEdRouteListener() {
        binding.edRoute.doAfterTextChanged { text ->
            viewModel.search(text.toString())
        }
    }

    private fun setBtnDeleteSearchKeywordListener(context: Activity) {
        binding.btnDeleteSearchKeyword.setOnClickListener {
            viewModel.clearKeyword()
            binding.edRoute.setText(viewModel.keyword)
            showSoftInput(binding.edRoute, context)
        }
    }

    private fun showSoftInput(view: View, context: Activity) {
        if (view.requestFocus()) {
            val imm = context.getSystemService(InputMethodManager::class.java)
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun setBtnDeleteAllRecentSearchListener(context: Activity) {
        binding.btnDeleteAllRecentSearch.setOnClickListener {
            viewModel.deleteAllRecentSearches(context)
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
        binding.llTimeout.visibility = if (view == binding.llTimeout) View.VISIBLE else View.GONE
        binding.rvSearchResult.visibility = if (view == binding.rvSearchResult) View.VISIBLE else View.GONE
        binding.pgbSearchRoute.visibility = if (view == binding.pgbSearchRoute) View.VISIBLE else View.GONE
        binding.txtNoResult.visibility = if (view == binding.txtNoResult) View.VISIBLE else View.GONE
        binding.llServiceError.visibility = if (view == binding.llServiceError) View.VISIBLE else View.GONE
    }
}
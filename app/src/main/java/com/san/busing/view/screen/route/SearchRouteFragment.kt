package com.san.busing.view.screen.route

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
import com.san.busing.data.repositoryimpl.route.RouteRepositoryImpl
import com.san.busing.data.source.local.provider.RoomDBProvider
import com.san.busing.data.source.remote.retrofit.provider.RetrofitProvider
import com.san.busing.databinding.FragmentSearchRouteBinding
import com.san.busing.domain.model.route.RouteModel
import com.san.busing.domain.modelimpl.route.RouteModels
import com.san.busing.domain.modelimpl.route.RouteRecentSearchModels
import com.san.busing.domain.state.UiState
import com.san.busing.domain.utils.Const
import com.san.busing.view.adapter.route.RouteRecentSearchAdapter
import com.san.busing.view.adapter.route.RouteSearchResultAdapter
import com.san.busing.view.listener.ItemClickEventListener
import com.san.busing.view.listener.RecyclerViewScrollListener
import com.san.busing.view.viewmodel.route.SearchRouteViewModel
import com.san.busing.view.viewmodelfactory.route.SearchRouteViewModelFactory
import com.san.busing.view.viewmodelimpl.route.SearchRouteViewModelImpl
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

        val repository =
            RouteRepositoryImpl(
                RetrofitProvider.getRouteService(),
                RetrofitProvider.getBusLocationService(),
                RoomDBProvider.get(requireActivity().applicationContext),
            )
        viewModel =
            ViewModelProvider(requireActivity(), SearchRouteViewModelFactory(repository)).get(
                SearchRouteViewModelImpl::class.java,
            )
    }

    /**
     * override fun onCreateView(): View
     *
     * 프레그먼트 생성 및 bottomNav 탭 전환 시 호출
     * 뷰 관련 작업 실행
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchRouteBinding.inflate(layoutInflater)

        initObserver(requireActivity())
        initListener(requireActivity())

        return binding.root
    }

    private fun initObserver(context: Activity) {
        viewModel.state.observe(
            viewLifecycleOwner,
            uiStateObserver(context),
        )
        viewModel.recentSearchContentReady.observe(
            viewLifecycleOwner,
            recentSearchContentReadyObserver(context),
        )
    }

    private fun uiStateObserver(activity: Activity) =
        Observer<UiState> {
            when (it) {
                UiState.Success -> {
                    if (viewModel.routes.isEmpty()) {
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

    private fun loadSearchResult(context: Activity) {
        binding.rvSearchResult.adapter =
            RouteSearchResultAdapter(
                viewModel.routes,
                searchResultItemClickEventListener(viewModel.routes, context),
                context,
            )
        binding.rvSearchResult.layoutManager = LinearLayoutManager(context)
        toggleView(binding.rvSearchResult)
    }

    private fun searchResultItemClickEventListener(
        items: RouteModels,
        activity: Activity,
    ) = object : ItemClickEventListener {
        override fun onItemClickListener(position: Int) {
            sendUserToRouteDetailScreen(activity, items.get(position))
        }

        override fun onDeleteButtonClickListener(position: Int) {}
    }

    private fun sendUserToRouteDetailScreen(
        activity: Activity,
        route: RouteModel,
    ) {
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

    private fun recentSearchContentReadyObserver(context: Activity) =
        Observer<Boolean> {
            if (it) {
                whenRecentSearchReady(context)
            } else {
                whenRecentSearchNotReady()
            }
        }

    private fun whenRecentSearchReady(context: Activity) {
        binding.rvRecentSearch.adapter =
            RouteRecentSearchAdapter(
                viewModel.routeRecentSearches,
                recentSearchItemClickEventListener(viewModel.routeRecentSearches, context),
                context,
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
        items: RouteRecentSearchModels,
        activity: Activity,
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
        setChkKeywordNightRoute()
        setBtnDeleteSearchKeywordListener(context)
        setBtnDeleteAllRecentSearchListener(context)
        setRvBusRouteScrollListener(context)
        setBtnRequestListener()
    }

    private fun setEdRouteListener() {
        binding.edRoute.doAfterTextChanged {
            val keyword = it.toString()

            if (isNightRoute(keyword) && !binding.chkKeywordNightRoute.isChecked) {
                binding.chkKeywordNightRoute.isChecked = true
            }
            if (isNotNightRoute(keyword) && binding.chkKeywordNightRoute.isChecked) {
                binding.chkKeywordNightRoute.isChecked = false
            }

            viewModel.search(keyword)
        }
    }

    private fun setChkKeywordNightRoute() {
        binding.chkKeywordNightRoute.setOnCheckedChangeListener { _, checked ->
            val keyword = binding.edRoute.text.toString()

            if (checked && isNotNightRoute(keyword)) binding.edRoute.setText(NIGHT_ROUTE_TAG + keyword)
            if (!checked && isNightRoute(keyword)) binding.edRoute.setText(keyword.removeRange(0..0))

            viewModel.search(binding.edRoute.text.toString())
        }
    }

    private fun isNightRoute(keyword: String) =
        keyword.isNotEmpty() && (
            keyword.first().toString() == NIGHT_ROUTE_TAG || keyword.first()
                .toString() == NIGHT_ROUTE_TAG.lowercase()
        )

    private fun isNotNightRoute(keyword: String) =
        keyword.isEmpty() || (
            keyword.first().toString() != NIGHT_ROUTE_TAG && keyword.first()
                .toString() != NIGHT_ROUTE_TAG.lowercase()
        )

    private fun setBtnDeleteSearchKeywordListener(context: Activity) {
        binding.btnDeleteSearchKeyword.setOnClickListener {
            viewModel.clearKeyword()
            binding.edRoute.setText(viewModel.keyword)
            binding.chkKeywordNightRoute.isChecked = false
            showSoftInput(binding.edRoute, context)
        }
    }

    private fun showSoftInput(
        view: View,
        context: Activity,
    ) {
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
        binding.pgbSearchRoute.visibility = visibleWhenTrue(view == binding.pgbSearchRoute)
        binding.rvSearchResult.visibility = visibleWhenTrue(view == binding.rvSearchResult)
        binding.txtNoResult.visibility = visibleWhenTrue(view == binding.txtNoResult)
        binding.llTimeout.visibility = visibleWhenTrue(view == binding.llTimeout)
        binding.llServiceError.visibility = visibleWhenTrue(view == binding.llServiceError)
    }

    private fun visibleWhenTrue(state: Boolean) = if (state) View.VISIBLE else View.GONE

    companion object {
        private const val NIGHT_ROUTE_TAG = "N"
    }
}

package com.san.busing.view.screen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.san.busing.BuildConfig
import com.san.busing.data.repositoryimpl.RouteRepositoryImpl
import com.san.busing.databinding.FragmentSearchRouteBinding
import com.san.busing.domain.model.RouteRecentSearchModel
import com.san.busing.domain.model.RouteSummaryModel
import com.san.busing.domain.utils.Const
import com.san.busing.domain.utils.Utils
import com.san.busing.domain.viewmodel.SearchRouteViewModel
import com.san.busing.domain.viewmodelfactory.SearchRouteViewModelFactory
import com.san.busing.domain.viewmodelimpl.SearchRouteViewModelImpl
import com.san.busing.view.adapter.RouteRecentSearchAdapter
import com.san.busing.view.adapter.RouteSearchResultAdapter
import com.san.busing.view.listener.ItemClickEventListener
import com.san.busing.view.listener.RecyclerViewScrollListener
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

        val repository = RouteRepositoryImpl(Utils.getRetrofit(BuildConfig.ROUTES_URL), requireActivity().applicationContext)
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

        initObserver(viewModel, requireActivity())
        initListener(viewModel, requireActivity())

        return binding.root
    }

    private fun initObserver(
        viewModel: SearchRouteViewModel,
        context: Activity
    ) {
        viewModel.searchResultContentReady.observe(
            viewLifecycleOwner,
            searchResultContentReadyObserver(viewModel, context)
        )
        viewModel.recentSearchContentReady.observe(
            viewLifecycleOwner,
            recentSearchContentReadyObserver(viewModel, context)
        )
        viewModel.serviceErrorState.observe(
            viewLifecycleOwner,
            serviceErrorStateObserver(viewModel, context)
        )
    }

    private fun searchResultContentReadyObserver(
        viewModel: SearchRouteViewModel,
        context: Activity
    ) = Observer<Boolean> {
        if (it) { whenSearchResultReady(viewModel, context) }
        else { whenSearchResultNotReady() }
    }

    private fun whenSearchResultReady(viewModel: SearchRouteViewModel, context: Activity) {
        binding.rvSearchResult.adapter = RouteSearchResultAdapter(
            viewModel.routeSummaries,
            searchResultItemClickEventListener(viewModel.routeSummaries, context),
            context
        )
        binding.rvSearchResult.layoutManager = LinearLayoutManager(context)
        binding.rvSearchResult.visibility = View.VISIBLE
    }

    private fun searchResultItemClickEventListener(
        items: List<RouteSummaryModel>,
        context: Activity
    ) = object : ItemClickEventListener {
        override fun onItemClickListener(position: Int) {
            val intent = Intent(context, RouteDetailActivity::class.java)
            intent.putExtra(Const.TAG_ROUTE_ID, items[position].id)
            intent.putExtra(Const.TAG_ROUTE_NAME, items[position].name)
            intent.putExtra(Const.TAG_ROUTE_TYPE, items[position].type)

            // 최근 검색 추가
            viewModel.insert(
                RouteRecentSearchModel(
                    items[position].id, items[position].name, items[position].type,
                    viewModel.recentSearchIndex(context)
                )
            )

            context.startActivity(intent)
        }

        override fun onDeleteButtonClickListener(position: Int) {}
    }

    private fun whenSearchResultNotReady() {
        binding.rvSearchResult.visibility = View.GONE
    }

    private fun recentSearchContentReadyObserver(
        viewModel: SearchRouteViewModel,
        context: Activity
    ) = Observer<Boolean> {
        if (it) { whenRecentSearchReady(viewModel, context) }
        else { whenRecentSearchNotReady() }
    }

    private fun whenRecentSearchReady(viewModel: SearchRouteViewModel, context: Activity) {
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
        items: List<RouteRecentSearchModel>,
        context: Activity
    ) = object : ItemClickEventListener {
        override fun onItemClickListener(position: Int) {
            val intent = Intent(context, RouteDetailActivity::class.java)
            intent.putExtra(Const.TAG_ROUTE_ID, items[position].id)
            intent.putExtra(Const.TAG_ROUTE_NAME, items[position].name)
            intent.putExtra(Const.TAG_ROUTE_TYPE, items[position].type)

            // 최근 검색 갱신
            viewModel.update(
                RouteRecentSearchModel(
                    items[position].id, items[position].name, items[position].type,
                    viewModel.recentSearchIndex(context)
                )
            )

            context.startActivity(intent)
        }

        override fun onDeleteButtonClickListener(position: Int) {
            viewModel.delete(items[position])
        }
    }

    private fun serviceErrorStateObserver(viewModel: SearchRouteViewModel, context: Activity) = Observer<Boolean> {
        if (it) {
            val toast = ErrorToast(context, viewModel.error)
            if (toast.previousFinished()) toast.show()
        }
    }

    private fun initListener(viewModel: SearchRouteViewModel, context: Activity) {
        setEdRouteActionListener(viewModel)
        setBtnDeleteSearchKeywordListener(viewModel, context)
        setBtnDeleteAllRecentSearchListener(viewModel, context)
        setRvBusRouteScrollListener(context)
    }

    private fun setEdRouteActionListener(viewModel: SearchRouteViewModel) {
        binding.edRoute.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.search(binding.edRoute.text.toString())
                return@setOnEditorActionListener true
            }

            return@setOnEditorActionListener false
        }
    }

    private fun setBtnDeleteSearchKeywordListener(viewModel: SearchRouteViewModel, context: Activity) {
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

    private fun setBtnDeleteAllRecentSearchListener(
        viewModel: SearchRouteViewModel,
        context: Activity
    ) {
        binding.btnDeleteAllRecentSearch.setOnClickListener {
            viewModel.deleteAll(context)
        }
    }

    private fun setRvBusRouteScrollListener(context: Activity) {
        binding.rvSearchResult.addOnScrollListener(RecyclerViewScrollListener(context))
    }

    override fun onStart() {
        super.onStart()
        restore(viewModel)
    }

    private fun restore(viewModel: SearchRouteViewModel) {
        binding.edRoute.setText(viewModel.keyword)
        viewModel.restore()
    }
}
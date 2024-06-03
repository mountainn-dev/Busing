package com.san.busing.view.screen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.BuildConfig
import com.san.busing.data.repositoryimpl.BusRouteRepositoryImpl
import com.san.busing.databinding.FragmentSearchRouteBinding
import com.san.busing.domain.model.BusRouteRecentSearchModel
import com.san.busing.domain.model.BusRouteSearchResultModel
import com.san.busing.domain.utils.Const
import com.san.busing.domain.utils.Utils
import com.san.busing.domain.viewmodel.SearchBusRouteViewModel
import com.san.busing.domain.viewmodelfactory.SearchBusRouteViewModelFactory
import com.san.busing.domain.viewmodelimpl.SearchBusRouteViewModelImpl
import com.san.busing.view.adapter.BusRouteRecentSearchAdapter
import com.san.busing.view.adapter.BusRouteSearchResultAdapter
import com.san.busing.view.listener.ItemClickEventListener
import com.san.busing.view.listener.RecyclerViewScrollListener

class SearchRouteFragment : Fragment() {
    private lateinit var binding: FragmentSearchRouteBinding
    private lateinit var viewModel: SearchBusRouteViewModel

    /**
     * override fun onCreate(): void
     *
     * 프레그먼트 탭 전환 시 onCreateView() 직전에 호출
     * 레포지토리 및 뷰모델 초기화 등 뷰 이전에 처리될 작업은 onCreate() 에서 진행
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = BusRouteRepositoryImpl(Utils.getRetrofit(BuildConfig.ROUTES_URL), requireActivity().applicationContext)
        viewModel = ViewModelProvider(requireActivity(), SearchBusRouteViewModelFactory(repository)).get(
            SearchBusRouteViewModelImpl::class.java
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
        loadContent(viewModel)

        return binding.root
    }

    private fun initObserver(
        viewModel: SearchBusRouteViewModel,
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
    }

    private fun searchResultContentReadyObserver(
        viewModel: SearchBusRouteViewModel,
        context: Activity
    ) = Observer<Boolean> {
        if (it) { whenSearchResultReady(viewModel, context) }
        else { whenSearchResultNotReady() }
    }

    private fun whenSearchResultReady(viewModel: SearchBusRouteViewModel, context: Activity) {
        binding.rvSearchResult.adapter = BusRouteSearchResultAdapter(
            viewModel.searchResultContent,
            searchResultItemClickEventListener(viewModel.searchResultContent, context),
            context
        )
        binding.rvSearchResult.layoutManager = LinearLayoutManager(context)
        binding.rvSearchResult.visibility = RecyclerView.VISIBLE
    }

    private fun searchResultItemClickEventListener(
        items: List<BusRouteSearchResultModel>,
        context: Activity
    ) = object : ItemClickEventListener {
        override fun onItemClickListener(position: Int) {
            val intent = Intent(context, BusRouteDetailActivity::class.java)
            intent.putExtra(Const.TAG_ROUTE_ID, items[position].id)
            intent.putExtra(Const.TAG_ROUTE_NAME, items[position].name)

            viewModel.update(
                BusRouteRecentSearchModel(
                    items[position].id, items[position].name, items[position].type,
                    viewModel.recentSearchIndex(context)
                )
            )

            context.startActivity(intent)
        }

        override fun onDeleteButtonClickListener(position: Int) {}
    }

    private fun whenSearchResultNotReady() {
        binding.rvSearchResult.visibility = RecyclerView.GONE
    }

    private fun recentSearchContentReadyObserver(
        viewModel: SearchBusRouteViewModel,
        context: Activity
    ) = Observer<Boolean> {
        if (it) { whenRecentSearchReady(viewModel, context) }
        else { whenRecentSearchNotReady() }
    }

    private fun whenRecentSearchReady(viewModel: SearchBusRouteViewModel, context: Activity) {
        binding.rvRecentSearch.adapter = BusRouteRecentSearchAdapter(
            viewModel.recentSearchContent,
            recentSearchItemClickEventListener(viewModel.recentSearchContent, context),
            context
        )
        binding.rvRecentSearch.layoutManager = LinearLayoutManager(
            activity, LinearLayoutManager.HORIZONTAL, false
        )
        binding.rvRecentSearch.visibility = RecyclerView.VISIBLE
    }

    private fun whenRecentSearchNotReady() {
        binding.rvRecentSearch.visibility = RecyclerView.GONE
    }

    private fun recentSearchItemClickEventListener(
        items: List<BusRouteRecentSearchModel>,
        context: Activity
    ) = object : ItemClickEventListener {
        override fun onItemClickListener(position: Int) {
            val intent = Intent(context, BusRouteDetailActivity::class.java)
            intent.putExtra(Const.TAG_ROUTE_ID, items[position].id)
            intent.putExtra(Const.TAG_ROUTE_NAME, items[position].name)
            context.startActivity(intent)
        }

        override fun onDeleteButtonClickListener(position: Int) {
            viewModel.delete(items[position])
        }
    }

    private fun initListener(viewModel: SearchBusRouteViewModel, context: Activity) {
        setEdRouteActionListener(viewModel)
        setBtnDeleteSearchKeywordListener(viewModel)
        setRvBusRouteScrollListener(context)
    }

    private fun setEdRouteActionListener(viewModel: SearchBusRouteViewModel) {
        binding.edRoute.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.search(binding.edRoute.text.toString())
                return@setOnEditorActionListener true
            }

            return@setOnEditorActionListener false
        }
    }

    private fun setBtnDeleteSearchKeywordListener(viewModel: SearchBusRouteViewModel) {
        binding.btnDeleteSearchKeyword.setOnClickListener {
            binding.edRoute.setText(Const.EMPTY_TEXT)
            viewModel.clear()
        }
    }

    private fun setRvBusRouteScrollListener(context: Activity) {
        binding.rvSearchResult.addOnScrollListener(RecyclerViewScrollListener(context))
    }

    private fun loadContent(viewModel: SearchBusRouteViewModel) {
        binding.edRoute.setText(viewModel.keyword)   // 검색 키워드 복원
        viewModel.restore()
    }
}
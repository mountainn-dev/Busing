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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchRouteBinding.inflate(layoutInflater)

        val repository = BusRouteRepositoryImpl(Utils.getRetrofit(BuildConfig.ROUTES_URL), requireActivity().applicationContext)
        viewModel = ViewModelProvider(requireActivity(), SearchBusRouteViewModelFactory(repository)).get(
            SearchBusRouteViewModelImpl::class.java
        )

        initObserver(viewModel)
        initListener(viewModel)

        return binding.root
    }

    private fun initObserver(viewModel: SearchBusRouteViewModel) {
        viewModel.searchResultContentReady.observe(viewLifecycleOwner, searchResultContentReadyObserver(viewModel))
        viewModel.recentSearchContentReady.observe(viewLifecycleOwner, recentSearchContentReadyObserver(viewModel))
    }

    private fun searchResultContentReadyObserver(viewModel: SearchBusRouteViewModel) = Observer<Boolean> {
        if (it) { whenSearchResultReady(viewModel) }
        else { whenSearchResultNotReady() }
    }

    private fun whenSearchResultReady(viewModel: SearchBusRouteViewModel) {
        binding.rvSearchResult.adapter = BusRouteSearchResultAdapter(
            viewModel.searchResultContent,
            searchResultItemClickEventListener(viewModel.searchResultContent),
            requireActivity())
        binding.rvSearchResult.layoutManager = layoutManager(viewModel)
        binding.rvSearchResult.visibility = RecyclerView.VISIBLE
    }

    private fun searchResultItemClickEventListener(items: List<BusRouteSearchResultModel>) = object: ItemClickEventListener {
        override fun onItemClickListener(position: Int) {
            val intent = Intent(requireActivity(), BusRouteDetailActivity::class.java)
            intent.putExtra(Const.TAG_ROUTE_ID, items[position].id)

            viewModel.updateRecentSearch(
                BusRouteRecentSearchModel(
                    items[position].id, items[position].name, items[position].type,
                    viewModel.recentSearchIndex(requireActivity())
                )
            )

            requireActivity().startActivity(intent)
        }

        override fun onDeleteButtonClickListener(position: Int) { }
    }

    private fun layoutManager(viewModel: SearchBusRouteViewModel): LinearLayoutManager {
        val manager = LinearLayoutManager(requireActivity())
        manager.onRestoreInstanceState(viewModel.getSearchResultViewInstanceState())

        return manager
    }

    private fun whenSearchResultNotReady() {
        binding.rvSearchResult.visibility = RecyclerView.INVISIBLE
    }

    private fun recentSearchContentReadyObserver(viewModel: SearchBusRouteViewModel) = Observer<Boolean> {
        if (it) { whenRecentSearchReady(viewModel) }
        else { whenRecentSearchNotReady() }
    }

    private fun whenRecentSearchReady(viewModel: SearchBusRouteViewModel) {
        binding.rvRecentSearch.adapter = BusRouteRecentSearchAdapter(
            viewModel.recentSearchContent,
            recentSearchItemClickEventListener(viewModel.recentSearchContent),
            requireActivity())
        binding.rvRecentSearch.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecentSearch.visibility = RecyclerView.VISIBLE
    }

    private fun whenRecentSearchNotReady() {
        binding.rvRecentSearch.visibility = RecyclerView.INVISIBLE
    }

    private fun recentSearchItemClickEventListener(items: List<BusRouteRecentSearchModel>) = object: ItemClickEventListener {
        override fun onItemClickListener(position: Int) {
            val intent = Intent(requireActivity(), BusRouteDetailActivity::class.java)
            intent.putExtra(Const.TAG_ROUTE_ID, items[position].id)
            requireActivity().startActivity(intent)
        }

        override fun onDeleteButtonClickListener(position: Int) {
            viewModel.delete(items[position])
        }
    }

    private fun initListener(viewModel: SearchBusRouteViewModel) {
        setEdRouteActionListener(viewModel)
        setBtnDeleteSearchKeywordListener(viewModel)
        setRvBusRouteScrollListener(requireActivity())
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
            viewModel.clearKeyword()
        }
    }

    private fun setRvBusRouteScrollListener(context: Activity) {
        binding.rvSearchResult.addOnScrollListener(RecyclerViewScrollListener(context))
    }

    override fun onStart() {
        super.onStart()
        loadContent(viewModel)
    }

    private fun loadContent(viewModel: SearchBusRouteViewModel) {
        binding.edRoute.setText(viewModel.keyword)   // 검색 키워드 복원
        viewModel.loadContent()
    }

    /**
     * override fun onStop(): void
     *
     * 노선 검색 화면으로부터 포커스가 이동할 때 호출
     */
    override fun onStop() {
        super.onStop()
        saveSearchResultViewInstanceState(viewModel)
    }

    private fun saveSearchResultViewInstanceState(viewModel: SearchBusRouteViewModel) {
        viewModel.setSearchResultViewInstanceState(binding.rvSearchResult.layoutManager?.onSaveInstanceState())
    }
}
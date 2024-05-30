package com.san.busing.view.screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
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
        if (it) {   // 검색 결과 목록 활성화
            binding.rvSearchResult.adapter = BusRouteSearchResultAdapter(
                viewModel.searchResultContent,
                searchResultItemClickEventListener(viewModel.searchResultContent),
                requireActivity())
            binding.rvSearchResult.layoutManager = LinearLayoutManager(activity)
            binding.rvSearchResult.visibility = RecyclerView.VISIBLE
        } else {   // 검색 결과 목록 비활성화
            binding.rvSearchResult.visibility = RecyclerView.INVISIBLE
        }
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

    private fun recentSearchContentReadyObserver(viewModel: SearchBusRouteViewModel) = Observer<Boolean> {
        if (it) {
            binding.rvRecentSearch.adapter = BusRouteRecentSearchAdapter(
                viewModel.recentSearchContent,
                recentSearchItemClickEventListener(viewModel.recentSearchContent),
                requireActivity())
            binding.rvRecentSearch.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            binding.rvRecentSearch.visibility = RecyclerView.VISIBLE
        } else {
            binding.rvRecentSearch.visibility = RecyclerView.INVISIBLE
        }
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

    private fun setRvBusRouteScrollListener(context: Context) {
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
}
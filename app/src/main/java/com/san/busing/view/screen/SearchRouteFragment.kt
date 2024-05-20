package com.san.busing.view.screen

import android.content.Context
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
import com.san.busing.domain.utils.RecyclerViewScrollListener
import com.san.busing.domain.utils.Utils
import com.san.busing.domain.viewmodelfactory.SearchBusRouteViewModelFactory
import com.san.busing.domain.viewmodelimpl.SearchBusRouteViewModelImpl
import com.san.busing.view.adapter.BusRouteViewAdapter

class SearchRouteFragment : Fragment() {
    private lateinit var binding: FragmentSearchRouteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchRouteBinding.inflate(layoutInflater)

        val repository = BusRouteRepositoryImpl(Utils.getRetrofit(BuildConfig.ROUTES_URL))
        val viewModel = ViewModelProvider(requireActivity(), SearchBusRouteViewModelFactory(repository)).get(
            SearchBusRouteViewModelImpl::class.java
        )

        initObserver(viewModel)
        initListener(viewModel)
        loadContent(viewModel)

        return binding.root
    }

    private fun initObserver(viewModel: SearchBusRouteViewModelImpl) {
        viewModel.contentReady.observe(viewLifecycleOwner, contentReadyObserver(viewModel))
    }

    private fun contentReadyObserver(viewModel: SearchBusRouteViewModelImpl) = Observer<Boolean> {
        if (it) {   // 검색 결과 리사이클러뷰 활성화
            binding.rvBusRoute.adapter = BusRouteViewAdapter(viewModel.content, requireActivity())
            binding.rvBusRoute.layoutManager = LinearLayoutManager(activity)
            binding.rvBusRoute.visibility = RecyclerView.VISIBLE
        } else {   // 검색 결과 리사이클러뷰 비활성화
            binding.rvBusRoute.visibility = RecyclerView.INVISIBLE
        }
    }

    private fun initListener(viewModel: SearchBusRouteViewModelImpl) {
        setEdRouteActionListener(viewModel)
        setRvBusRouteScrollListener(requireActivity())
    }

    private fun setEdRouteActionListener(viewModel: SearchBusRouteViewModelImpl) {
        binding.edRoute.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.search(binding.edRoute.text.toString())
                return@setOnEditorActionListener true
            }

            return@setOnEditorActionListener false
        }
    }

    private fun setRvBusRouteScrollListener(context: Context) {
        binding.rvBusRoute.addOnScrollListener(Utils.getRecyclerViewScrollListener(context))
    }

    private fun loadContent(viewModel: SearchBusRouteViewModelImpl) {
        binding.edRoute.setText(viewModel.keyword)   // 검색 키워드 복원
        if (!viewModel.content.isEmpty()) {   // 검색 결과 복원
            binding.rvBusRoute.adapter = BusRouteViewAdapter(viewModel.content, requireActivity())
            binding.rvBusRoute.layoutManager = LinearLayoutManager(activity)
            binding.rvBusRoute.visibility = RecyclerView.VISIBLE
        }
    }
}
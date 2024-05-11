package com.san.busing.view.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.BuildConfig
import com.san.busing.R
import com.san.busing.data.repositoryimpl.BusRouteRepositoryImpl
import com.san.busing.databinding.FragmentSearchRouteBinding
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
        val viewModel = ViewModelProvider(this, SearchBusRouteViewModelFactory(repository)).get(
            SearchBusRouteViewModelImpl::class.java
        )

        initObserver(viewModel)
        binding.btnSearch.setOnClickListener {
            viewModel.search(binding.edRoute.text.toString())
        }

        return binding.root
    }

    private fun initObserver(viewModel: SearchBusRouteViewModelImpl) {
        viewModel.searchCompleted.observe(viewLifecycleOwner, searchResultObserver(viewModel))
    }

    private fun searchResultObserver(viewModel: SearchBusRouteViewModelImpl) = Observer<Boolean> {
        if (it) {   // 검색 결과 리사이클러뷰 활성화
            binding.rvBusRoute.adapter = BusRouteViewAdapter(viewModel.content)
            binding.rvBusRoute.layoutManager = LinearLayoutManager(activity)
            binding.rvBusRoute.visibility = RecyclerView.VISIBLE
        } else {   // 검색 결과 리사이클러뷰 비활성화
            binding.rvBusRoute.visibility = RecyclerView.INVISIBLE
        }
    }
}
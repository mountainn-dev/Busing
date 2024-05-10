package com.san.busing.view.screen

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.san.busing.BuildConfig
import com.san.busing.data.repositoryimpl.BusRouteRepositoryImpl
import com.san.busing.databinding.ActivityHomeBinding
import com.san.busing.domain.utils.Utils
import com.san.busing.domain.viewmodelfactory.SearchBusRouteViewModelFactory
import com.san.busing.domain.viewmodelimpl.SearchBusRouteViewModelImpl
import com.san.busing.view.adapter.BusRouteViewAdapter
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = BusRouteRepositoryImpl(Utils.getRetrofit(BuildConfig.ROUTES_URL))
        val viewModel = ViewModelProvider(this, SearchBusRouteViewModelFactory(repository)).get(
            SearchBusRouteViewModelImpl::class.java
        )

        initObserver(viewModel)
        binding.btnSearch.setOnClickListener {
            viewModel.search(binding.edRoute.text.toString())
        }
    }

    private fun initObserver(viewModel: SearchBusRouteViewModelImpl) {
        viewModel.searchCompleted.observe(this, searchResultObserver(viewModel))
    }

    private fun searchResultObserver(viewModel: SearchBusRouteViewModelImpl) = Observer<Boolean> {
        if (it) {   // 검색 결과 리사이클러뷰 활성화
            binding.rvBusRoute.adapter = BusRouteViewAdapter(viewModel.content)
            binding.rvBusRoute.layoutManager = LinearLayoutManager(this)
            binding.rvBusRoute.visibility = RecyclerView.VISIBLE
        } else {   // 검색 결과 리사이클러뷰 비활성화
            binding.rvBusRoute.visibility = RecyclerView.INVISIBLE
        }
    }
}
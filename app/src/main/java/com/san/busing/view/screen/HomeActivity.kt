package com.san.busing.view.screen

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.san.busing.BuildConfig
import com.san.busing.data.BusServiceInterceptor
import com.san.busing.data.repositoryimpl.BusRouteRepositoryImpl
import com.san.busing.databinding.ActivityHomeBinding
import com.san.busing.domain.viewmodelfactory.SearchBusRouteViewModelFactory
import com.san.busing.domain.viewmodelimpl.SearchBusRouteViewModelImpl
import com.san.busing.view.adapter.BusRouteViewAdapter
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {
    private val client = OkHttpClient.Builder()
        .addInterceptor(BusServiceInterceptor)
        .build()
    private val retrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(BuildConfig.ROUTES_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
    private val repository = BusRouteRepositoryImpl(retrofit)
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ViewModelProvider(this, SearchBusRouteViewModelFactory(repository)).get(SearchBusRouteViewModelImpl::class.java)
        val searchResultObserver = Observer<Boolean> {
            if (it) {
                binding.txtNoResult.visibility = TextView.INVISIBLE
                binding.rvBusRoute.visibility = RecyclerView.VISIBLE
                binding.rvBusRoute.adapter = BusRouteViewAdapter(viewModel.content)
                binding.rvBusRoute.layoutManager = LinearLayoutManager(this)
            } else {
                binding.rvBusRoute.visibility = RecyclerView.INVISIBLE
                binding.txtNoResult.visibility = TextView.INVISIBLE
                Log.d("result", "${viewModel.error}")
            }
        }

        viewModel.searchCompleted.observe(this, searchResultObserver)
        binding.btnSearch.setOnClickListener {
            viewModel.search(binding.edRoute.text.toString())
        }
    }

    private fun initObserver() {

    }
}
package com.san.busing.view.screen.route

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.san.busing.databinding.ActivityRouteInfoBinding
import com.san.busing.domain.modelimpl.route.RouteInfoModel
import com.san.busing.domain.utils.Const

class RouteInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRouteInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRouteInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val routeInfo = intent.getSerializableExtra(Const.TAG_ROUTE_INFO) as RouteInfoModel

        initToolbar(routeInfo.name)
        initListener()
        load(routeInfo)
    }

    private fun initToolbar(routeName: String) {
        binding.txtTitle.text = routeName
    }

    private fun initListener() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun load(routeInfo: RouteInfoModel) {
        loadRouteRange(routeInfo)
        loadRouteTime(routeInfo)
        loadRouteInterval(routeInfo)
    }

    private fun loadRouteRange(routeInfo: RouteInfoModel) {
        binding.txtStartStation.text = routeInfo.startStationName
        binding.txtEndStation.text = routeInfo.endStationName
    }

    private fun loadRouteTime(routeInfo: RouteInfoModel) {
        binding.txtStartFirstTime.text = routeInfo.startFirstTime?.toString() ?: NO_TIME_DATA
        binding.txtStartLastTime.text = routeInfo.startLastTime?.toString() ?: NO_TIME_DATA
        binding.txtEndFirstTime.text = routeInfo.endFirstTime?.toString() ?: NO_TIME_DATA
        binding.txtEndLastTime.text = routeInfo.endLastTime?.toString() ?: NO_TIME_DATA
    }

    private fun loadRouteInterval(routeInfo: RouteInfoModel) {
        val intervalMessage =
            if (routeInfo.maxPeekAlloc == Const.ZERO) {
                NO_INTERVAL_DATA_MESSAGE
            } else {
                String.format(MAX_INTERVAL_MESSAGE, routeInfo.maxPeekAlloc)
            }
        binding.txtInterval.text = intervalMessage
    }

    companion object {
        private const val NO_TIME_DATA = "-"
        private const val NO_INTERVAL_DATA_MESSAGE = "최대 -분"
        private const val MAX_INTERVAL_MESSAGE = "최대 %d분"
    }
}

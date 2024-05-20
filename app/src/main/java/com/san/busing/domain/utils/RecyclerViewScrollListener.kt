package com.san.busing.domain.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 리사이클러뷰 스크롤 리스너
 *
 * onScrollStateChanged - 뷰 데이터 갱신과 상관없이 스크롤 상태가 변경되는 경우에 대해서만 콜백
 * onScrolled - 뷰 데이터가 갱신되는 순간을 포함하여 스크롤 상태가 변경되는 경우에 콜백
 */
class RecyclerViewScrollListener(private val context: Context) : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        val imm = context.getSystemService(InputMethodManager::class.java)
        imm.hideSoftInputFromWindow(recyclerView.windowToken, 0)   // 소프트 키보드 비활성화
    }
}
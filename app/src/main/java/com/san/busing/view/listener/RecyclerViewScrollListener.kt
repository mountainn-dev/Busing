package com.san.busing.view.listener

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
    private val manager: InputMethodManager = context.getSystemService(InputMethodManager::class.java)

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (isSoftInputActive(recyclerView)) hideSoftInput(recyclerView)
    }

    /**
     * isSoftInputActive(recyclerView: RecyclerView): Boolean
     *
     * hideSoftInputFromWindow() 는 IMM 소프트 키보드 활성화 여부에 따라 boolean 을 리턴한다.
     * 이를 이용하여 키보드 비활성 작업이 중복적으로 발생하는 것을 방지한다.
     */
    private fun isSoftInputActive(recyclerView: RecyclerView) = manager.hideSoftInputFromWindow(
        recyclerView.windowToken, 0)

    private fun hideSoftInput(recyclerView: RecyclerView) {
        manager.hideSoftInputFromWindow(recyclerView.windowToken, 0)
    }
}
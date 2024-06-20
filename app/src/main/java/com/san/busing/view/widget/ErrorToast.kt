package com.san.busing.view.widget

import android.content.Context
import android.widget.Toast
import com.san.busing.domain.utils.Const

/**
 * ErrorToast
 *
 * Service 에러 관련 피드백을 제공하기 위한 Toast 클래스
 */
class ErrorToast(context: Context) : Toast(context) {
    private var toastedTime: Long = Const.ZERO.toLong()

    init {
        super.setText(Const.SERVICE_ERROR)
    }

    override fun show() {
        toastedTime = System.currentTimeMillis()
        super.show()
    }

    fun previousFinished() = (System.currentTimeMillis() - toastedTime) > TOAST_SHORT

    companion object {
        private const val TOAST_SHORT: Long = 2
    }
}
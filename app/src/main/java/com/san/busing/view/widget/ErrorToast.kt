package com.san.busing.view.widget

import android.content.Context
import android.widget.Toast

/**
 * ErrorToast
 *
 * Service 에러 관련 피드백을 제공하기 위한 Toast 클래스
 * min api 레벨 제한으로 Hidden 콜백 기능은 직접 구현
 */
class ErrorToast(context: Context, message: CharSequence) : Toast(context) {
    init {
        super.setText(message)
    }

    override fun show() {
        toastedTime = System.currentTimeMillis() / MILLIS_DIVIDER
        super.show()
    }

    fun previousFinished() = (System.currentTimeMillis() / MILLIS_DIVIDER) - toastedTime > TOAST_SHORT

    companion object {
        private const val TOAST_SHORT = 2
        private const val MILLIS_DIVIDER = 1000
        private var toastedTime: Long = 0
    }
}
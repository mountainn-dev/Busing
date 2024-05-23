package com.san.busing.view.listener

import android.view.View

interface ItemClickEventListener {
    fun onItemClickListener(position: Int)

    fun onDeleteButtonClickListener(position: Int)
}
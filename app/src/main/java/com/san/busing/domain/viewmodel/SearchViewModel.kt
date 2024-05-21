package com.san.busing.domain.viewmodel

import androidx.lifecycle.LiveData

interface SearchViewModel {
    val contentReady: LiveData<Boolean>

    fun search(keyword: String)
}
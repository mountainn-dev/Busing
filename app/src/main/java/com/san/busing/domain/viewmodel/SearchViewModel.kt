package com.san.busing.domain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface SearchViewModel {
    val searchCompleted: LiveData<Boolean>

    fun search(keyword: String)
}
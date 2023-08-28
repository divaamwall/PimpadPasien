package com.diva.pimpad.ui.resultdetect

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diva.pimpad.model.ResultDetect
import com.diva.pimpad.repository.ResultDetectRepository

class ResultDetectViewModel: ViewModel() {
    private val repository: ResultDetectRepository
    val allResultDetect = MutableLiveData<ArrayList<ResultDetect>>()

    init {
        repository = ResultDetectRepository().getInstance()
        repository.loadResultDetect(allResultDetect)
    }
}
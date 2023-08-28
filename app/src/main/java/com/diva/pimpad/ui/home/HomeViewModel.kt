package com.diva.pimpad.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diva.pimpad.model.Acne
import com.diva.pimpad.repository.AcneRepository

class HomeViewModel : ViewModel() {

    private val repository : AcneRepository
    val allAcnes = MutableLiveData<ArrayList<Acne>>()

    init {
        repository = AcneRepository().getInstance()
        repository.loadAcnes(allAcnes)
    }
}
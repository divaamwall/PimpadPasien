package com.diva.pimpad.ui.listchat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.diva.pimpad.model.Dokters
import com.diva.pimpad.repository.UserRepository

class ChatViewModel : ViewModel() {

    private val repository : UserRepository
    val allDokters = MutableLiveData<ArrayList<Dokters>>()

    init {
        repository = UserRepository().getInstance()
        repository.loadDokters(allDokters)
    }
}
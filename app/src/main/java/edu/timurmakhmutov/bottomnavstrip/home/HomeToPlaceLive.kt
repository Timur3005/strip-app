package edu.timurmakhmutov.bottomnavstrip.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeToPlaceLive : ViewModel() {
    private val _trueId = MutableLiveData<String>()
    val trueId: LiveData<String> get() = _trueId

    fun setTrueId(id: String) {
        _trueId.value = id
    }
}

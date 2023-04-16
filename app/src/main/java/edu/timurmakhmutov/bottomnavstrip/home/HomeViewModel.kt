package edu.timurmakhmutov.bottomnavstrip.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    val liveDataHome = MutableLiveData<List<HomePlaceNames>>()
}
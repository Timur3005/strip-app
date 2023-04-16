package edu.timurmakhmutov.bottomnavstrip.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    public val liveDataHome = MutableLiveData<List<HomePlaceNames>>()
}
package edu.timurmakhmutov.bottomnavstrip.place_screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.timurmakhmutov.bottomnavstrip.home.HomePlaceNames

class PlaceViewModel: ViewModel() {
    val liveDataCommentsFields: MutableLiveData<List<PlaceCommentsFields>> by lazy {
        MutableLiveData<List<PlaceCommentsFields>>()
    }
}
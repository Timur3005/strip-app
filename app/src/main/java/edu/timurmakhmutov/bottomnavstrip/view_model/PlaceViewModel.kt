package edu.timurmakhmutov.bottomnavstrip.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.timurmakhmutov.bottomnavstrip.place_screen.PlaceCommentsFields

class PlaceViewModel: ViewModel() {
    val liveDataCommentsFields: MutableLiveData<List<PlaceCommentsFields>> by lazy {
        MutableLiveData<List<PlaceCommentsFields>>()
    }
}
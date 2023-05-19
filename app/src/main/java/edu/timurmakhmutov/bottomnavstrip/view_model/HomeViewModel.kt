package edu.timurmakhmutov.bottomnavstrip.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.timurmakhmutov.bottomnavstrip.home.HomePlaceNames
import edu.timurmakhmutov.bottomnavstrip.home.ToursNames

class HomeViewModel : ViewModel() {

    val liveDataHome: MutableLiveData<List<HomePlaceNames>> by lazy {
        MutableLiveData<List<HomePlaceNames>>()
    }
    val liveDataFind: MutableLiveData<List<HomePlaceNames>> by lazy {
        MutableLiveData<List<HomePlaceNames>>()
    }
    val liveDataToursNames: MutableLiveData<List<ToursNames>> by lazy {
        MutableLiveData<List<ToursNames>>()
    }

    val placesLoaded = MutableLiveData<Boolean>()
    val toursLoaded = MutableLiveData<Boolean>()
    val uId = MutableLiveData<String>()
    val wasEnter = MutableLiveData<String>()
    val searchPlace = MutableLiveData<String>()
}
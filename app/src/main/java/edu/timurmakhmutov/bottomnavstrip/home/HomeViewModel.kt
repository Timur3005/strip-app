package edu.timurmakhmutov.bottomnavstrip.home

import android.os.AsyncTask.Status
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.ChoiceFormat

class HomeViewModel : ViewModel() {

    val liveDataHome: MutableLiveData<List<HomePlaceNames>> by lazy {
        MutableLiveData<List<HomePlaceNames>>()
    }
}
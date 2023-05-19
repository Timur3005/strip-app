package edu.timurmakhmutov.bottomnavstrip.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.timurmakhmutov.bottomnavstrip.DataBase.TableForDB

internal class LKViewModel : ViewModel(){
    val liveDataLiked: MutableLiveData<List<TableForDB>> by lazy {
        MutableLiveData<List<TableForDB>>()
    }
    val liveDataPath: MutableLiveData<List<TableForDB>> by lazy {
        MutableLiveData<List<TableForDB>>()
    }
}
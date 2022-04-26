package com.androidassessment.phonegallery.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.androidassessment.phonegallery.model.Exhibit
import com.androidassessment.phonegallery.model.LoadingState
import com.androidassessment.phonegallery.repository.RestExhibitLoaderRepository
import com.androidassessment.phonegallery.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private var viewModelJob = Job()
private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

class MainActivityViewModel : ViewModel() {
    val exhibitList = mutableStateListOf<Exhibit>()

    fun fetchExhibitListFromServer(context: Context, loadingState: (loadingState: LoadingState) -> Unit) {
        uiScope.launch {
            val list = RestExhibitLoaderRepository().getExhibitList()
            if(list.isEmpty()){
                if(!Utils.isInternetConnected(context)){
                    loadingState(LoadingState.NoInternet)
                }else {
                    loadingState(LoadingState.Error)
                }
                exhibitList.removeAll(exhibitList)
                exhibitList.add(Exhibit(title = "Error Occured", emptyArray()))
            }else{
                exhibitList.removeAll(exhibitList)
                exhibitList.addAll(list)
                loadingState(LoadingState.Done)
            }
        }
    }
}
package com.androidassessment.phonegallery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainActivityViewModel::class.java)){
            return MainActivityViewModel() as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}
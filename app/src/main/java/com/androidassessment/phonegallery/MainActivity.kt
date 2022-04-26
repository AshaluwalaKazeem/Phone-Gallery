package com.androidassessment.phonegallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import com.androidassessment.phonegallery.ui.pages.GalleryScreen
import com.androidassessment.phonegallery.ui.theme.PhoneGalaryTheme
import com.androidassessment.phonegallery.viewmodel.MainActivityViewModel
import com.androidassessment.phonegallery.viewmodel.ViewModelFactory

@ExperimentalMaterialApi
@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // I will be using Jetpack Compose (Android modern toolkit for building native UI) to design the UI for this app since it simplifies and accelerates UI development on Android.
        val viewModelFactory = ViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)

        setContent {
            PhoneGalaryTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    GalleryScreen(viewModel = viewModel)
                }
            }
        }
    }
}
package com.androidassessment.phonegallery.api

import com.androidassessment.phonegallery.model.Exhibit
import retrofit2.Call
import retrofit2.http.GET

interface ExhibitLoader {

    @GET("list")
    fun getExhibitList(): Call<List<Exhibit>>
}
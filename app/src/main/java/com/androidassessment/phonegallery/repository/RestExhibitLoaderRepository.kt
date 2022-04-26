package com.androidassessment.phonegallery.repository

import com.androidassessment.phonegallery.api.RetrofitInstance
import com.androidassessment.phonegallery.model.Exhibit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RestExhibitLoaderRepository {

    suspend fun getExhibitList(): List<Exhibit> =
        withContext(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getExhibitList()
                val data = response.execute()
                if (data.isSuccessful) return@withContext data.body()!!
                return@withContext emptyList()
            } catch (e: Exception) {
                return@withContext emptyList()
            }
        }
}
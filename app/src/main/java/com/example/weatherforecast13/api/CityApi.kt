package com.example.weatherforecast13.api

import com.example.weatherforecast13.data.City
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CityApi {
    @GET("v1/city")
    suspend fun getCityInfo(
        @Query("name") cityName: String,
        @Header("X-Api-Key") apiKey: String
    ): List<City>
}
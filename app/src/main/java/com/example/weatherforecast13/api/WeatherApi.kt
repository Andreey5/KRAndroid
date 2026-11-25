package com.example.weatherforecast13.api
import com.example.weatherforecast13.data.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("v1/forecast")
    suspend fun getWeatherForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("hourly") hourlyParams: String = "temperature_2m",
        @Query("forecast_days") days: Int = 1
    ): WeatherResponse
}
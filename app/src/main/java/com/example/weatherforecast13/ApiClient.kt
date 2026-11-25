package com.example.weatherforecast13

import com.example.weatherforecast13.data.City
import com.example.weatherforecast13.data.WeatherResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val CITY_API_BASE_URL = "https://api.api-ninjas.com/"
    private const val WEATHER_API_BASE_URL = "https://api.open-meteo.com/"
    private const val API_NINJAS_KEY = "jndWDDkMD9OCqd7wqJPIjQ==7wK7i78tuZa3oH2x" // В реальном проекте храните ключ безопасно

    // 1. Убедимся, что интерфейсы импортированы правильно
    private val cityApi: com.example.weatherforecast13.api.CityApi by lazy {
        Retrofit.Builder()
            .baseUrl(CITY_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(com.example.weatherforecast13.api.CityApi::class.java)
    }

    private val weatherApi: com.example.weatherforecast13.api.WeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl(WEATHER_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(com.example.weatherforecast13.api.WeatherApi::class.java)
    }

    suspend fun getCityCoordinates(cityName: String): City? {
        return try {
            cityApi.getCityInfo(
                cityName = cityName,
                apiKey = API_NINJAS_KEY
            ).firstOrNull()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getWeatherForecast(city: City): WeatherResponse {
        return weatherApi.getWeatherForecast(
            latitude = city.latitude,
            longitude = city.longitude
        )
    }
}
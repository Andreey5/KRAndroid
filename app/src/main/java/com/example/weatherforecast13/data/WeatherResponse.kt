package com.example.weatherforecast13.data

data class WeatherResponse(
    val hourly: HourlyWeather  // Данные почасового прогноза
)

data class HourlyWeather(
    val time: List<String>,               // Список временных меток
    val temperature_2m: List<Double>     // Список температур
)
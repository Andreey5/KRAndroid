package com.example.weatherforecast13.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast13.ApiClient
import com.example.weatherforecast13.data.City
import com.example.weatherforecast13.data.WeatherResponse
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val _cities = MutableLiveData<List<City>>(emptyList())
    val cities: LiveData<List<City>> = _cities

    private val _weatherData = MutableLiveData<Map<String, WeatherResponse>>(emptyMap())
    val weatherData: LiveData<Map<String, WeatherResponse>> = _weatherData

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    fun addCity(cityName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val city = ApiClient.getCityCoordinates(cityName)
                city?.let {
                    val currentList = _cities.value.orEmpty().toMutableList()
                    if (!currentList.any { c -> c.name.equals(cityName, ignoreCase = true) }) {
                        currentList.add(it)
                        _cities.value = currentList
                        fetchWeatherForCity(it)
                    } else {
                        _errorMessage.value = "Город уже в списке"
                    }
                } ?: run {
                    _errorMessage.value = "Город не найден"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Ошибка сети: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun fetchWeatherForCity(city: City) {
        viewModelScope.launch {
            try {
                val weather = ApiClient.getWeatherForecast(city)
                val currentData = _weatherData.value.orEmpty().toMutableMap()
                currentData[city.name] = weather
                _weatherData.value = currentData
            } catch (e: Exception) {
                _errorMessage.value = "Не удалось получить погоду для ${city.name}"
            }
        }
    }

    fun removeCity(cityName: String) {
        val currentList = _cities.value.orEmpty().toMutableList()
        currentList.removeAll { it.name == cityName }
        _cities.value = currentList

        val currentData = _weatherData.value.orEmpty().toMutableMap()
        currentData.remove(cityName)
        _weatherData.value = currentData
    }
}
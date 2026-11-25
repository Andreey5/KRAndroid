package com.example.weatherforecast13

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast13.adapter.CityAdapter
import com.example.weatherforecast13.databinding.ActivityMainBinding
import com.example.weatherforecast13.viewmodel.WeatherViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: WeatherViewModel
    private lateinit var adapter: CityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerView() {
        adapter = CityAdapter(
            emptyList(),
            emptyMap(),
            onRemoveClick = { cityName ->
                viewModel.removeCity(cityName)
            }
        )

        binding.citiesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun setupObservers() {
        viewModel.cities.observe(this) { cities ->
            adapter = CityAdapter(
                cities,
                viewModel.weatherData.value ?: emptyMap(),
                onRemoveClick = { cityName ->
                    viewModel.removeCity(cityName)
                }
            )
            binding.citiesRecyclerView.adapter = adapter
        }

        viewModel.weatherData.observe(this) { weatherData ->
            adapter = CityAdapter(
                viewModel.cities.value ?: emptyList(),
                weatherData,
                onRemoveClick = { cityName ->
                    viewModel.removeCity(cityName)
                }
            )
            binding.citiesRecyclerView.adapter = adapter
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun setupListeners() {
        binding.addCityButton.setOnClickListener {
            binding.cityInputEditText.text?.let { editable ->
                val cityName = editable.toString().trim()
                if (cityName.isNotEmpty()) {
                    viewModel.addCity(cityName)
                    editable.clear()
                }
            }
        }
    }
}
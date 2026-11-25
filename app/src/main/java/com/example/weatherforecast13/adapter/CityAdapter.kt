package com.example.weatherforecast13.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast13.R
import com.example.weatherforecast13.data.City
import com.example.weatherforecast13.data.WeatherResponse

class CityAdapter(
    private val cities: List<City>,
    private val weatherData: Map<String, WeatherResponse>,
    private val onRemoveClick: (String) -> Unit
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cityNameTextView: TextView = itemView.findViewById(R.id.cityNameTextView)
        private val temperatureTextView: TextView = itemView.findViewById(R.id.temperatureTextView)
        private val removeButton: ImageButton = itemView.findViewById(R.id.removeButton)

        fun bind(city: City) {
            cityNameTextView.text = city.name

            weatherData[city.name]?.let { weather ->
                val currentTemp = weather.hourly.temperature_2m.firstOrNull()
                temperatureTextView.text = if (currentTemp != null) {
                    "${currentTemp}°C"
                } else {
                    "N/A"
                }
            } ?: run {
                temperatureTextView.text = "Загрузка..."
            }

            removeButton.setOnClickListener {
                onRemoveClick(city.name)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_city, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind(cities[position])
    }

    override fun getItemCount(): Int = cities.size
}
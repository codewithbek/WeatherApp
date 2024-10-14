package com.example.weatherapp.presentation.home_page

import com.example.weatherapp.domain.weather.WeatherData
import com.example.weatherapp.domain.weather.WeatherInfo

data class WeatherState(
    val weatherInfo: WeatherInfo? = null,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedCity: String = "Tashkent", // Default city
    val cityWeatherData: Map<String, WeatherData> = emptyMap() // Weather data for multiple cities
) {

}

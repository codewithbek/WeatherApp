package com.example.weatherapp.presentation.home_page

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.domain.location.LocationTracker
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.domain.util.Resource
import com.example.weatherapp.domain.weather.WeatherData
import kotlinx.coroutines.launch



class WeatherViewModel(
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker,
) : ViewModel() {

    var state by mutableStateOf(WeatherState())

    private val cityCoordinates = mapOf(
        "London" to Pair(51.5072, -0.1278),
        "New York" to Pair(40.7128, -74.0060),
        "Bejing" to Pair(39.9042, 116.4074),
    )

    // Called when a city is selected
    fun onCitySelected(city: String) {
        state = state.copy(selectedCity = city)
        loadWeatherForCity(city) // Load weather for the selected city
    }


    // Function to load weather data for a specific city
    private fun loadWeatherForCity(city: String) {
        val coordinates = cityCoordinates[city]
        coordinates?.let { (latitude, longitude) ->
            viewModelScope.launch {
                state = state.copy(isLoading = true, error = null) // Set loading state
                when (val result = repository.getWeatherData(latitude, longitude)) {
                    is Resource.Success -> {
                        val weatherInfo = result.data // Assuming result.data is of type WeatherInfo
                        state = state.copy(
                            weatherInfo = weatherInfo, // Update weatherInfo with the fetched data
                            isLoading = false,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        state = state.copy(
                            weatherInfo = null,
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }

    // Function to load weather data for all cities
    fun loadWeatherForCities() {
        viewModelScope.launch {
            val weatherDataMap = mutableMapOf<String, WeatherData>()
            cityCoordinates.forEach { (city, coordinates) ->
                val (latitude, longitude) = coordinates
                when (val result = repository.getWeatherData(latitude, longitude)) {
                    is Resource.Success -> {
                        val weatherInfo = result.data // Assuming result.data is of type WeatherInfo
                        val weatherData = weatherInfo?.currentWeatherData
                            ?: weatherInfo?.weatherDataPerDay?.values?.flatten()?.firstOrNull()

                        weatherData?.let { data ->
                            weatherDataMap[city] = data // Store the weather data for each city
                        }
                    }
                    is Resource.Error -> {
                        state = state.copy(
                            error = result.message
                        )
                    }
                }
            }
            // Update the state with all the loaded city weather data
            state = state.copy(
                cityWeatherData = weatherDataMap,
                isLoading = false
            )
        }
    }

    // Function to load weather info based on the user's location
    fun loadWeatherInfo() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            locationTracker.getCurrentLocation()?.let { location ->
                state = state.copy(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
                when (val result = repository.getWeatherData(location.latitude, location.longitude)) {
                    is Resource.Success -> {
                        state = state.copy(
                            weatherInfo = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        state = state.copy(
                            weatherInfo = null,
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            } ?: run {
                state = state.copy(
                    isLoading = false,
                    error = "Couldn't retrieve location. Make sure to grant permission and enable GPS."
                )
            }
        }
    }
}

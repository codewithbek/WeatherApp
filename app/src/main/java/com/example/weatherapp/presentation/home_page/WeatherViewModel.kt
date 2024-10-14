package com.example.weatherapp.presentation.home_page


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.domain.util.Resource
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository,
//    private val locationTracker: LocationTracker,
) : ViewModel() {

    var state by mutableStateOf(WeatherState())

    init {
        loadWeatherInfo()
    }


    private fun loadWeatherInfo() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            when (val result = repository.getWeatherData(53.9006, 27.5590)) {
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
//            locationTracker.getCurrentLocation()?.let { location ->
//                when (val result = repository.getWeatherData(location.latitude, location.longitude)) {
//                    is Resource.Success -> {
//                        state = state.copy(
//                            weatherInfo = result.data,
//                            isLoading = false,
//                            error = null
//                        )
//                    }
//
//                    is Resource.Error -> {
//                        state = state.copy(
//                            weatherInfo = null,
//                            isLoading = false,
//                            error = result.message
//                        )
//                    }
//                }
//            } ?: kotlin.run {
//                state = state.copy(
//                    isLoading = false,
//                    error = "Couldn't retrieve location. Make sure to grant permission and enable GPS."
//                )
//            }
        }
    }
}
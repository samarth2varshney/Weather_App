package com.example.weather_app.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather_app.Resource
import com.example.weather_app.domain.location.LocationTracker
import com.example.weather_app.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker
): ViewModel() {

    var state by mutableStateOf(WeatherState())
        private set

    fun loadWeatherInfo() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            locationTracker.getCurrentLocation()?.let { location ->
                Log.i("samarth","${location.latitude} ${location.longitude}")
                when(val result = repository.getWeatherData(location.latitude, location.longitude)) {
                    is Resource.Success -> {
                        Log.i("samarth","${result.data}")
                        state = state.copy(
                            weatherInfo = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        Log.i("samarth","error message ${result.message}")
                        state = state.copy(
                            weatherInfo = null,
                            isLoading = false,
                            error = result.message
                        )
                    }

                    else -> {

                    }
                }
            } ?: kotlin.run {
                state = state.copy(
                    isLoading = false,
                    error = "Couldn't retrieve location. Make sure to grant permission and enable GPS."
                )
            }
        }
    }
}
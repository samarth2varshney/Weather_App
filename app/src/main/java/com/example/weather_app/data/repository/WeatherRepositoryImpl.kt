package com.example.weather_app.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weather_app.Resource
import com.example.weather_app.data.mappers.toWeatherInfo
import com.example.weather_app.data.remote.WeatherApi
import com.example.weather_app.domain.repository.WeatherRepository
import com.example.weather_app.domain.weather.WeatherInfo
import javax.inject.Inject
import kotlin.Exception

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
) : WeatherRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo> {
        return try {
            Resource.Success(
                data = api.getWeatherData(
                    lat = lat,
                    long = long
                ).toWeatherInfo()
            )
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error")
        }
    }
}
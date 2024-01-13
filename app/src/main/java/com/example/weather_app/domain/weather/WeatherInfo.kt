package com.example.weather_app.domain.weather

data class WeatherInfo(
    val weatherDataPerDay: Map<Int,List<WeatherData>>,
    val currentWeatherData: WeatherData?
)

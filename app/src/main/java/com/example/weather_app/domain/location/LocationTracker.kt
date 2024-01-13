package com.example.weather_app.domain.location

import android.location.Location

interface LocationTracker {

    suspend fun getCurrentLocation(): Location?

}
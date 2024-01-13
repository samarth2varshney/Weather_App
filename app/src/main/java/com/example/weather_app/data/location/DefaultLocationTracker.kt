package com.example.weather_app.data.location

import android.app.Application
import android.location.Location
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import com.example.weather_app.domain.location.LocationTracker
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

@ExperimentalCoroutinesApi
class DefaultLocationTracker @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val application: Application
):LocationTracker {

    override suspend fun getCurrentLocation(): Location? {

        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if(!hasAccessCoarseLocationPermission || !hasAccessFineLocationPermission || !isGpsEnabled){
            return null
        }

        return suspendCancellableCoroutine { cont->
            locationClient.lastLocation.apply {
                if(isComplete){
                    if(isSuccessful)
                        cont.resume(result)
                    else
                        cont.resume(null)

                    return@suspendCancellableCoroutine
                }
                addOnSuccessListener {
                    cont.resume(it)
                }
                addOnFailureListener {
                    cont.resume(null)
                }
                addOnCanceledListener {
                    cont.cancel()
                }
            }
        }

    }

}
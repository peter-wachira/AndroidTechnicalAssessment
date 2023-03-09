package com.example.dirverapp.utils

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject

class UpdateLocation @Inject constructor(
    private val client: FusedLocationProviderClient,
) {
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(): Flow<Location> = callbackFlow {
        val locationRequest = LocationRequest.create().apply {
            interval = UPDATE_INTERVAL
            fastestInterval = FASTEST_UPDATE_INTERVAL
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val callBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location = locationResult.lastLocation
                if (location != null) {
                    trySend(location)
                }
            }
        }
        client.requestLocationUpdates(locationRequest, callBack, Looper.getMainLooper())

        awaitClose {
            Timber.e("location updates removed")
            client.removeLocationUpdates(callBack)
        }
    }

    @SuppressLint("MissingPermission")
    fun fetchContinuousUpdates(): Flow<Location> = callbackFlow {
        val locationRequest = LocationRequest.create().apply {
            interval = 5000L
            fastestInterval = interval
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val callBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location = locationResult.lastLocation
                Timber.e("updated location $location")
                if (location != null) {
                    trySend(location)
                }
            }
        }
        client.requestLocationUpdates(locationRequest, callBack, Looper.getMainLooper())
        awaitClose { client.removeLocationUpdates(callBack) }
    }

    companion object {
        private const val UPDATE_INTERVAL = 3000L
        private const val FASTEST_UPDATE_INTERVAL = 3000L
    }
}

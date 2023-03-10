package com.example.dirverapp.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.* // ktlint-disable no-wildcard-imports
import android.graphics.drawable.BitmapDrawable
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import androidx.annotation.Keep
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import com.example.dirverapp.BuildConfig
import com.example.dirverapp.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

sealed class LocationException {
    data class PermissionDeniedException(val errorMessage: String) : LocationException()
    data class RationaleException(val errorMessage: String) : LocationException()
}

typealias ErrorMessage = (LocationException) -> Unit

@Keep
enum class LocationAction {
    PERMISSION, GPS
}

fun Context.isLocationEnabled(): Boolean {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return LocationManagerCompat.isLocationEnabled(locationManager)
}

fun Context.isLocationPermissionEnabled(): Boolean {
    return ActivityCompat.checkSelfPermission(
        applicationContext,
        Manifest.permission.ACCESS_FINE_LOCATION,
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.getLocationPermission(onPermissionAccepted: (() -> Unit), errorMessage: ErrorMessage) {
    val permissions = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    Dexter.withContext(this)
        .withPermissions(permissions)
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                report?.let { result ->
                    if (result.areAllPermissionsGranted()) {
                        onPermissionAccepted.invoke()
                    } else {
                        errorMessage.invoke(LocationException.PermissionDeniedException("Location Permission denied"))
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(requests: MutableList<PermissionRequest>?, token: PermissionToken?) {
                token?.continuePermissionRequest()
                errorMessage.invoke(LocationException.RationaleException("allow location permission"))
            }
        }).check()
}

fun Activity.showLocationDialog(locationAction: LocationAction) {
    MaterialAlertDialogBuilder(this).apply {
        val title = if (locationAction == LocationAction.GPS) "Location Disabled" else "Location Permissions Denied"

        setCancelable(false)
        setTitle(title)
        setMessage(if (locationAction == LocationAction.GPS) "Open Settings to enable location" else "Open application details to enable location permissions.")
        setPositiveButton("Continue") { dialog, _ ->
            dialog.dismiss()

            when (locationAction) {
                LocationAction.PERMISSION -> {
                    val packageName = BuildConfig.APPLICATION_ID
                    dialog.dismiss()
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.parse("package:$packageName")
                        startActivity(this)
                    }
                }
                LocationAction.GPS -> {
                    dialog.dismiss()
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
                        startActivity(this)
                    }
                }
            }
        }
        show()
    }
}

const val MAP_CAMERA_ZOOM = 10F

fun GoogleMap.moveCameraWithAnim(latLng: LatLng) {
    animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, MAP_CAMERA_ZOOM))
}

fun Context.getShopIcon(): BitmapDescriptor {
    val vehicleIcon = ContextCompat.getDrawable(this, R.drawable.shop_icon_png) as BitmapDrawable

    val color = ContextCompat.getColor(this, R.color.colorAccent)

    val smallMarker = Bitmap.createScaledBitmap(vehicleIcon.bitmap, 80, 80, false)

    val paint = Paint()
    val canvas = Canvas(smallMarker)

    paint.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(smallMarker, 0F, 0F, paint)
    return BitmapDescriptorFactory.fromBitmap(smallMarker)
}

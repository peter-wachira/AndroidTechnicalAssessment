package com.example.dirverapp.ui.map

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.dirverapp.R
import com.example.dirverapp.data.remote.directions.Area
import com.example.dirverapp.data.remote.orders.OrderEntity
import com.example.dirverapp.databinding.FragmentMapBinding
import com.example.dirverapp.other.* // ktlint-disable no-wildcard-imports
import com.example.dirverapp.ui.list.DeliveriesViewModel
import com.example.dirverapp.utils.* // ktlint-disable no-wildcard-imports
import com.example.dirverapp.utils.showErrorDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.* // ktlint-disable no-wildcard-imports
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {
    private val binding: FragmentMapBinding by lazy {
        FragmentMapBinding.inflate(layoutInflater)
    }
    private var orders: MutableList<OrderEntity> = ArrayList()
    private lateinit var mMap: GoogleMap

    private lateinit var latLngBounds: LatLngBounds
    private val viewModel: DeliveriesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = binding.root

    override fun onResume() {
        super.onResume()
        if (binding.root.context.isLocationPermissionEnabled()) {
            if (binding.root.context.isLocationEnabled()) {
                getLastLocation()
            } else {
                showLocationDialog()
            }
        } else {
            // request location Permission
            requestLocationPermission()
        }

        observeViewModel()
        val mapFragment = childFragmentManager.findFragmentById(binding.mapView.id) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun observeViewModel() {
        viewModel.currentLocation.observe(viewLifecycleOwner) { location ->
            val latLng = LatLng(location.latitude, location.longitude)
            Timber.e("current location $location")
            if (::mMap.isInitialized) {
                mMap.moveCameraWithAnim(latLng)
                setupMarkers()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setupMap()
    }

    @SuppressLint("MissingPermission")
    private fun setupMap() {
        if (binding.root.context.isLocationPermissionEnabled()) {
            mMap.apply {
                clear()
                setMinZoomPreference(5F)

                isBuildingsEnabled = false
                uiSettings.isRotateGesturesEnabled = true
                uiSettings.isMapToolbarEnabled = false
                uiSettings.isMyLocationButtonEnabled = false
                isMyLocationEnabled = false

                val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                    binding.root.context,
                    R.raw.default_maps_style,
                )
                setMapStyle(mapStyleOptions)
                getLocations()
                viewModel.getLocation()
            }
            getGeoLayer()
        } else {
            requestLocationPermission()
        }
    }

    private fun getGeoLayer() {
        viewModel.getAreaGeoPoints().observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResponse.Success -> {
                    addGeoLayer(response.value.areas[0])
                    Timber.e("getAreaGeoPoints response ${response.value}")
                }

                is ApiResponse.Failure -> {
                    activity?.showToast("unable to get sales area details")
                }
            }
        }
    }

    private fun addGeoLayer(salesAreaDetailsResponse: Area) {
        val coordinates: List<List<Double>> = salesAreaDetailsResponse.territory.coordinates[0]

        val coordinatesList: ArrayList<LatLng> = arrayListOf()

        for (coord in coordinates) {
            coordinatesList.add(LatLng(coord[1], coord[0]))
        }

        mMap.addPolyline(
            PolylineOptions()
                .addAll(coordinatesList)
                .width(4F)
                .geodesic(true)
                .color(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)),
        )

        latLngBounds = LatLngBounds.builder().apply {
            for (coordinate in coordinatesList) {
                include(coordinate)
            }
        }.build()

        with(mMap) {
            animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 20))
        }
    }

    private fun getLastLocation() {
        lifecycleScope.launchWhenStarted {
            viewModel.getLocation()
        }
    }

    private fun showLocationDialog() {
        activity?.showErrorDialog(
            "Location Disabled",
            "Your Location seems to be turned off",
            true,
        ) {
            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
                startActivity(this)
            }
        }
    }

    private fun getLocations() {
        lifecycleScope.launch {
            viewModel.observeAllOrderItems().collect() { orders ->
                if (orders is ApiResponse.Success) {
                    orders.value
                    this@MapFragment.orders = orders.value.orders.map { it.toOrderEntity() }.toMutableList()
                    Timber.e("orders $orders") }
            }
        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    private fun setupMarkers() {
        for (order in orders) {
            val orderLocation = LatLng(order.latitude, order.longitude)

            val markerOption = MarkerOptions().apply {
                position(orderLocation)
                title(order.customerName)
                icon(binding.root.context.getShopIcon())
            }

            with(mMap) {
                val marker = addMarker(markerOption)
                marker?.snippet = order.orderBatchNumber

                setOnMarkerClickListener { selectedMarker ->

                    val ordersFilter = orders.filter { it.latitude == selectedMarker.position.latitude }

                    Timber.e("matching orders $ordersFilter")

                    if (ordersFilter.isNotEmpty()) {
                        showDialog(ordersFilter.first())
                    }

                    return@setOnMarkerClickListener true
                }
            }
        }
    }

    private fun showDialog(order: OrderEntity) {
        val markerDetailsBottomSheet = MarkerDetailsBottomSheet(order)
        markerDetailsBottomSheet.isCancelable = true
        markerDetailsBottomSheet.show(parentFragmentManager.beginTransaction(), "DETAILS")
    }

    private fun requestLocationPermission() {
        activity?.getLocationPermission(
            onPermissionAccepted = {
                setupMap()
            },

            errorMessage = { locationException ->
                when (locationException) {
                    is LocationException.PermissionDeniedException -> {
                        activity?.showLocationDialog(LocationAction.PERMISSION)
                    }
                    is LocationException.RationaleException -> {
                        binding.root.showErrorSnackbar(locationException.errorMessage, Snackbar.LENGTH_LONG)
                    }
                }
            },
        )
    }
}

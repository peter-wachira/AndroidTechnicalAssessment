package com.example.dirverapp.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.dirverapp.R
import com.example.dirverapp.databinding.ActivityDeliveriesBinding
import com.example.dirverapp.utils.LocationException
import com.example.dirverapp.utils.getLocationPermission
import com.example.dirverapp.utils.isLocationPermissionEnabled
import com.example.dirverapp.utils.showRetrySnackBar
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeliveriesActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val binding: ActivityDeliveriesBinding by lazy {
        ActivityDeliveriesBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.deliveriesToolbar)
        setupDrawer()
    }

    private fun setupDrawer() {
        val drawerToggle = object : ActionBarDrawerToggle(this, binding.drawerLayoutDeliveries, binding.deliveriesToolbar, R.string.open, R.string.close) {}
        binding.drawerLayoutDeliveries.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        binding.deliveriesToolbar.setNavigationOnClickListener {
            binding.drawerLayoutDeliveries.openDrawer(GravityCompat.START)
        }

        binding.deliveriesNav.setNavigationItemSelectedListener(this)

        setupNavigation()
    }

    private fun requestLocationPermission() {
        getLocationPermission(
            onPermissionAccepted = {},
            errorMessage = { locationException ->
                if (locationException is LocationException.RationaleException) {
                    binding.root.showRetrySnackBar(locationException.errorMessage) { requestLocationPermission() }
                }
            },
        )
    }

    private fun setupNavigation() {
        binding.deliveriesToolbar.setNavigationOnClickListener {
            binding.drawerLayoutDeliveries.openDrawer(GravityCompat.START)
        }
        if (!isLocationPermissionEnabled()) {
            requestLocationPermission()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayoutDeliveries.closeDrawer(GravityCompat.START)
        return true
    }
}

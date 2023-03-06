package com.example.dirverapp.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.dirverapp.R
import com.example.dirverapp.databinding.ActivityDeliveriesBinding
import com.google.android.material.navigation.NavigationView

class DeliveriesActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val binding: ActivityDeliveriesBinding by lazy {
        ActivityDeliveriesBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.deliveriesToolbar)
        observeViewModel()

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

    private fun setupNavigation() {
        binding.deliveriesToolbar.setNavigationOnClickListener {
            binding.drawerLayoutDeliveries.openDrawer(GravityCompat.START)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // Switch different options
        }

        binding.drawerLayoutDeliveries.closeDrawer(GravityCompat.START)
        return true
    }

    private fun observeViewModel() {
        TODO("Not yet implemented")
    }
}

package com.example.dirverapp.ui.list

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.dirverapp.data.remote.OrderItemsResponse
import com.example.dirverapp.databinding.FragmentDeliveriesBinding
import com.example.dirverapp.other.* // ktlint-disable no-wildcard-imports
import com.example.dirverapp.utils.* // ktlint-disable no-wildcard-imports
import com.example.dirverapp.utils.showErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class DeliveriesFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private val binding: FragmentDeliveriesBinding by lazy {
        FragmentDeliveriesBinding.inflate(layoutInflater)
    }

    private val viewModel: DeliveriesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root

    override fun onStart() {
        super.onStart()
        Timber.e("on start called")
        initObservers()
        setupViews()
    }

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

    private fun getLastLocation() {
        lifecycleScope.launchWhenStarted {
            viewModel.getLocation()
        }
    }

    fun setupViews() {
        binding.root.setOnRefreshListener(this)
    }

    private fun initObservers() {
        viewModel.getOrders()

        viewModel.orders.observe(viewLifecycleOwner) { response ->
            handlePreOrders(response)
        }

        lifecycleScope.launchWhenCreated {
            viewModel.isLoading.collect { isLoading ->

                when (isLoading) {
                    true -> {
                        binding.root.isRefreshing = true
                    }

                    false -> {
                        if (binding.root.isRefreshing) {
                            binding.root.isRefreshing = false
                        }
                    }
                }
            }
        }
    }

    private fun handlePreOrders(response: ApiResponse<OrderItemsResponse>) {
        when (response) {
            is ApiResponse.Success -> {
                Timber.e("Deliveries: ${response.value}")

                val ordersAdapter = OrdersAdapter()

                binding.ordersRecycler.apply {
                    adapter = ordersAdapter
                }

                if (response.value.orders.isNotEmpty()) {
                    binding.apply {
                        imgEmptyList.hide()
                        tvEmpty.hide()
                    }
                } else {
                    binding.apply {
                        imgEmptyList.show()
                        tvEmpty.show()
                    }
                }
            }

            is ApiResponse.Failure -> {
                binding.root.showErrorSnackbar(response.errorHolder.message)
            }
        }
    }

    private fun requestLocationPermission() {
        activity?.getLocationPermission(
            onPermissionAccepted = {
                getLastLocation()
            },
            errorMessage = { locationException ->
                if (locationException is LocationException.RationaleException) {
                    binding.root.showRetrySnackBar(locationException.errorMessage) { requestLocationPermission() }
                }
            },
        )
    }

    override fun onRefresh() {
        if (binding.root.context.isLocationPermissionEnabled()) {
            if (binding.root.context.isLocationEnabled()) {
                getLastLocation()
            } else {
                activity?.showLocationDialog(LocationAction.GPS)
            }
        } else {
            requestLocationPermission()
        }
    }
}

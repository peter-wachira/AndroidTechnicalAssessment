package com.example.dirverapp.ui.list

import android.location.Location
import androidx.lifecycle.* // ktlint-disable no-wildcard-imports
import com.example.dirverapp.data.remote.orders.OrderItemsResponse
import com.example.dirverapp.other.toOrderEntity
import com.example.dirverapp.utils.ApiResponse
import com.example.dirverapp.utils.UpdateLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveriesViewModel @Inject constructor(
    private val updateLocation: UpdateLocation,
    private val repository: DeliveriesRepositoryInterface,
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _orders = MutableLiveData<ApiResponse<OrderItemsResponse>>()
    val orders: LiveData<ApiResponse<OrderItemsResponse>> = _orders

    private val _currentLocation: MutableLiveData<Location> = MutableLiveData()
    val currentLocation: LiveData<Location>
        get() = _currentLocation

    fun getLocation() = viewModelScope.launch {
        _isLoading.value = true
        updateLocation.getCurrentLocation().collect { lastLocation ->
            _currentLocation.value = lastLocation
            cancel("stop location updates")
        }
        _isLoading.value = false
    }

    fun getOrders() = viewModelScope.launch(IO) {
        _isLoading.value = true
        val ordersDeferred = async {
            repository.getOrders()
        }
        val orders = ordersDeferred.await()
        insertToDB(orders)
        if (orders is ApiResponse.Success) {
            val allOrders = orders.value
            _orders.postValue(ApiResponse.Success(allOrders))
        }
        _isLoading.value = false
    }

    private suspend fun insertToDB(orders: ApiResponse<OrderItemsResponse>) = viewModelScope.launch {
        if (orders is ApiResponse.Success) {
            val ordersList = orders.value
            ordersList.orders.forEach {
                repository.insertOrder(it.toOrderEntity())
            }
        }
    }

    fun observeAllOrderItems() = flow {
        emit(repository.getOrders())
    }

    fun getAreaGeoPoints() = liveData {
        emit(repository.getGeoPoints())
    }
}

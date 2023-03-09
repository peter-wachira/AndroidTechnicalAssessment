package com.example.dirverapp.ui.list

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dirverapp.data.remote.OrderEntity
import com.example.dirverapp.data.remote.OrderItemsResponse
import com.example.dirverapp.other.Event
import com.example.dirverapp.other.Resource
import com.example.dirverapp.other.toOrderEntity
import com.example.dirverapp.utils.ApiResponse
import com.example.dirverapp.utils.UpdateLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveriesViewModel @Inject constructor(
    private val updateLocation: UpdateLocation,
    private val repository: DeliveriesRepositoryInterface,
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val orderItems = repository.observeAllOrders()

    private val _orders = MutableLiveData<ApiResponse<OrderItemsResponse>>()
    val orders: LiveData<ApiResponse<OrderItemsResponse>> = _orders

    private val _insertOrderItems = MutableLiveData<Event<Resource<OrderEntity>>>()
    val insertOrderItems: LiveData<Event<Resource<OrderEntity>>> = _insertOrderItems

    private val _currentLocation: MutableLiveData<Location> = MutableLiveData()
    val currentLocation: LiveData<Location>
        get() = _currentLocation

    fun deleteOrderItem(orderEntity: OrderEntity) = viewModelScope.launch {
        repository.deleteOrderItem(orderEntity)
    }

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
    }

    private suspend fun insertToDB(orders: ApiResponse<OrderItemsResponse>) = viewModelScope.launch {
        if (orders is ApiResponse.Success) {
            val ordersList = orders.value
            ordersList.orders.forEach {
                repository.insertOrder(it.toOrderEntity())
            }
        }
    }
}

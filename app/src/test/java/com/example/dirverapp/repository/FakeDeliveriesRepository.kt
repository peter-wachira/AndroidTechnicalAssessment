package com.example.dirverapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dirverapp.data.remote.directions.SalesAreasResponse
import com.example.dirverapp.data.remote.orders.OrderEntity
import com.example.dirverapp.data.remote.orders.OrderItemsResponse
import com.example.dirverapp.ui.list.DeliveriesRepositoryInterface
import com.example.dirverapp.utils.ApiResponse
import com.example.dirverapp.utils.ErrorHolder

class FakeDeliveriesRepository : DeliveriesRepositoryInterface {

    private val orderItems = mutableListOf<OrderEntity>()
    private val observeAllOrderItems = MutableLiveData<List<OrderEntity>>(orderItems)

    private fun refreshLiveData() {
        observeAllOrderItems.postValue(orderItems)
    }

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    override fun observeAllOrders(): LiveData<List<OrderEntity>> {
        return observeAllOrderItems
    }

    override suspend fun insertOrder(orderEntity: OrderEntity) {
        orderItems.add(orderEntity)
    }

    override suspend fun deleteOrderItem(orderEntity: OrderEntity) {
        orderItems.remove(orderEntity)
    }

    override suspend fun getOrders(): ApiResponse<OrderItemsResponse> {
        return if (shouldReturnNetworkError) {
            ApiResponse.Failure(ErrorHolder("Error", null))
        } else {
            ApiResponse.Success(OrderItemsResponse(listOf()))
        }
    }

    override suspend fun getGeoPoints(): ApiResponse<SalesAreasResponse> {
        return if (shouldReturnNetworkError) {
            ApiResponse.Failure(ErrorHolder("Error", null))
        } else {
            ApiResponse.Success(SalesAreasResponse(listOf()))
        }
    }
}

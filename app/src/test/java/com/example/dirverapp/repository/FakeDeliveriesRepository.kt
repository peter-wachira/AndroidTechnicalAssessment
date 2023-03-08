package com.example.dirverapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dirverapp.data.remote.OrderEntity
import com.example.dirverapp.data.remote.OrderItemsResponse
import com.example.dirverapp.other.Resource
import com.example.dirverapp.ui.list.DeliveriesRepositoryInterface

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

    override suspend fun observeAllOrders(): LiveData<List<OrderEntity>> {
        return observeAllOrderItems
    }

    override suspend fun insertOrder(orderEntity: OrderEntity) {
        orderItems.add(orderEntity)
    }

    override suspend fun deleteOrderItem(orderEntity: OrderEntity) {
        orderItems.remove(orderEntity)
    }

    override suspend fun getOrders(): Resource<OrderItemsResponse> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error", null)
        } else {
            Resource.success(OrderItemsResponse(listOf()))
        }
    }
}

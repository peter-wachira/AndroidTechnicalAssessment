package com.example.dirverapp.ui.list

import androidx.lifecycle.LiveData
import com.example.dirverapp.data.remote.OrderEntity
import com.example.dirverapp.data.remote.OrderItemsResponse
import com.example.dirverapp.utils.ApiResponse

interface DeliveriesRepositoryInterface {
    fun observeAllOrders(): LiveData<List<OrderEntity>>
    suspend fun insertOrder(orderEntity: OrderEntity)
    suspend fun deleteOrderItem(orderEntity: OrderEntity)
    suspend fun getOrders(): ApiResponse<OrderItemsResponse>
}

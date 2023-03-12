package com.example.dirverapp.ui.list

import androidx.lifecycle.LiveData
import com.example.dirverapp.data.remote.directions.SalesAreasResponse
import com.example.dirverapp.data.remote.orders.OrderEntity
import com.example.dirverapp.data.remote.orders.OrderItemsResponse
import com.example.dirverapp.utils.ApiResponse

interface DeliveriesRepositoryInterface {
    fun observeAllOrders(): LiveData<List<OrderEntity>>
    suspend fun insertOrder(orderEntity: OrderEntity)
    suspend fun deleteOrderItem(orderEntity: OrderEntity)
    suspend fun getOrders(): ApiResponse<OrderItemsResponse>
    suspend fun getGeoPoints(): ApiResponse<SalesAreasResponse>
}

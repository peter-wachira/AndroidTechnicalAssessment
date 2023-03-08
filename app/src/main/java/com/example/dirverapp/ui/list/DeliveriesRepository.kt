package com.example.dirverapp.ui.list

import androidx.lifecycle.LiveData
import com.example.dirverapp.data.local.OrdersDao
import com.example.dirverapp.data.remote.MockAPI
import com.example.dirverapp.data.remote.OrderEntity
import com.example.dirverapp.data.remote.OrderItemsResponse
import com.example.dirverapp.other.Resource
import com.example.dirverapp.other.toOrderEntity
import javax.inject.Inject

class DeliveriesRepository @Inject constructor(
    private val dao: OrdersDao,
    private val mockAPI: MockAPI,
) : DeliveriesRepositoryInterface {

    override suspend fun observeAllOrders(): LiveData<List<OrderEntity>> {
        return dao.observeAllOrders()
    }

    override suspend fun insertOrder(orderEntity: OrderEntity) {
        return dao.insertOrder(orderEntity)
    }

    override suspend fun deleteOrderItem(orderEntity: OrderEntity) {
        return dao.deleteOrderItem(orderEntity)
    }

    override suspend fun getOrders(): Resource<OrderItemsResponse> {
        return try {
            val response = mockAPI.getOrders()
            if (response.isSuccessful) {
                response.body()?.let { ordersItemResponse ->
                    ordersItemResponse.orders.forEach { order ->
                        insertOrder(order.toOrderEntity())
                    }
                    return@let Resource.success(ordersItemResponse)
                } ?: Resource.error("An unknown error occured", null)
            } else {
                Resource.error("An unknown error occured", null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't reach the server. Check your internet connection", null)
        }
    }
}

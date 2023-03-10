package com.example.dirverapp.ui.list

import androidx.lifecycle.LiveData
import com.example.dirverapp.data.local.OrdersDao
import com.example.dirverapp.data.remote.MockAPI
import com.example.dirverapp.data.remote.orders.OrderEntity
import com.example.dirverapp.data.repository.BaseRepository
import javax.inject.Inject

class DeliveriesRepository @Inject constructor(
    private val dao: OrdersDao,
    private val mockAPI: MockAPI,
) : DeliveriesRepositoryInterface, BaseRepository() {

    override fun observeAllOrders(): LiveData<List<OrderEntity>> {
        return dao.observeAllOrders()
    }

    override suspend fun insertOrder(orderEntity: OrderEntity) {
        return dao.insertOrder(orderEntity)
    }

    override suspend fun deleteOrderItem(orderEntity: OrderEntity) {
        return dao.deleteOrderItem(orderEntity)
    }

    override suspend fun getOrders() = apiCall {
        mockAPI.getOrders()
    }

    override suspend fun getGeoPoints() = apiCall {
        mockAPI.getSalesAreaDetails()
    }
}

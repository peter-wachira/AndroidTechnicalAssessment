package com.example.dirverapp.data.remote // ktlint-disable filename

import com.example.dirverapp.data.remote.directions.SalesAreasResponse
import com.example.dirverapp.data.remote.orders.OrderItemsResponse
import retrofit2.http.GET

interface MockAPI {
    @GET("/v3/c7bb0f75-a033-48b0-90d2-385111fe15c2")
    suspend fun getOrders(): OrderItemsResponse

    @GET("/v3/9b36445d-25d1-4f97-9ac5-4123466ee298")
    suspend fun getSalesAreaDetails(): SalesAreasResponse
}

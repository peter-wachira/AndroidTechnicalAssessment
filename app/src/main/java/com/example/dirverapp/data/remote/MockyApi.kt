package com.example.dirverapp.data.remote // ktlint-disable filename

import com.example.dirverapp.data.remote.directions.SalesAreasResponse
import com.example.dirverapp.data.remote.orders.OrderItemsResponse
import retrofit2.http.GET

interface MockAPI {
    @GET("/v3/c7bb0f75-a033-48b0-90d2-385111fe15c2")
    suspend fun getOrders(): OrderItemsResponse

    @GET("/v3/fe850595-fd0e-4ca9-88e6-b6dc17d6d795")
    suspend fun getSalesAreaDetails(): SalesAreasResponse
}

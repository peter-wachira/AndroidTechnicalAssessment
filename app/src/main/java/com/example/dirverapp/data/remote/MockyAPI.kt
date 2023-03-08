package com.example.dirverapp.data.remote // ktlint-disable filename

import retrofit2.Response
import retrofit2.http.GET

interface MockAPI {
    @GET("/v3/c6ebac36-be13-4314-8a48-6555989b9696")
    suspend fun getOrders(): Response<OrderItemsResponse>
}

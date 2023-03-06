package com.example.dirverapp.data.remote // ktlint-disable filename

import retrofit2.Response
import retrofit2.http.GET

interface MockAPI {
    @GET("/v3/48211ca6-9e5b-49c2-8e68-c7816127cf62")
    suspend fun getOrders(): Response<OrderItemsResponse>
}

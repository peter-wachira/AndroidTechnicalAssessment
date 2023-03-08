package com.example.dirverapp.data.remote

import com.google.gson.annotations.SerializedName

data class OrderItemsResponse(
    @SerializedName("orders")
    val orders: List<Order>,
)

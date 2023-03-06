package com.example.dirverapp.data.remote

import com.google.gson.annotations.SerializedName

data class OrderItemsResponseItem(
    @SerializedName("customerCode")
    val customerCode: String,
    @SerializedName("customerName")
    val customerName: String,
    @SerializedName("customerTypeCode")
    val customerTypeCode: String,
    @SerializedName("dateCreated")
    val dateCreated: String,
    @SerializedName("deliveryLocations")
    val deliveryLocations: DeliveryLocations,
    @SerializedName("orderBatchNumber")
    val orderBatchNumber: String,
    @SerializedName("orderStatus")
    val orderStatus: String,
    @SerializedName("primaryPhone")
    val primaryPhone: String,
    @SerializedName("secondaryPhone")
    val secondaryPhone: String,
)

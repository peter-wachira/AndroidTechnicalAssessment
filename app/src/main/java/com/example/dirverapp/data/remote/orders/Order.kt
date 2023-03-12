package com.example.dirverapp.data.remote.orders

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("address")
    val address: String,
    @SerializedName("customerCode")
    val customerCode: String,
    @SerializedName("customerName")
    val customerName: String,
    @SerializedName("customerTyCode")
    val customerTyCode: String,
    @SerializedName("deliverLocationName")
    val deliverLocationName: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("orderBatchNumber")
    val orderBatchNumber: String,
    @SerializedName("orderStatus")
    val orderStatus: String,
    @SerializedName("primaryPhone")
    val primaryPhone: String,
    @SerializedName("secondaryPhone")
    val secondaryPhone: String,
)

@Entity(tableName = "orders")
data class OrderEntity(
    val customerName: String,
    val address: String,
    @PrimaryKey(autoGenerate = false)
    val orderBatchNumber: String,
    val primaryPhone: String,
    val secondaryPhone: String?,
    val latitude: Double,
    val longitude: Double,
    val deliverLocationName: String,
    val orderStatus: String,
    val customerTyCode: String,
    val customerCode: String,
)

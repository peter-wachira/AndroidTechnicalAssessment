package com.example.dirverapp.data.remote

import com.google.gson.annotations.SerializedName

data class DeliveryLocations(
    @SerializedName("addressLine1")
    val addressLine1: String,
    @SerializedName("addressLine2")
    val addressLine2: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("country")
    val country: String,
)

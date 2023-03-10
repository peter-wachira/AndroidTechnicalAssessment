package com.example.dirverapp.data.remote.directions

import com.google.gson.annotations.SerializedName

data class Area(
    @SerializedName("Name")
    val name: String,
    @SerializedName("SalesAreaCode")
    val salesAreaCode: String,
    @SerializedName("Territory")
    val territory: Territory,
)

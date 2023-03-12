package com.example.dirverapp.data.remote.directions

import com.google.gson.annotations.SerializedName

data class Territory(
    @SerializedName("coordinates")
    val coordinates: List<List<List<Double>>>,
    @SerializedName("type")
    val type: String,
)

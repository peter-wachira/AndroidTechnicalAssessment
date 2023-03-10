package com.example.dirverapp.data.remote.directions

import com.google.gson.annotations.SerializedName

data class SalesAreasResponse(
    @SerializedName("areas")
    val areas: List<Area>,
)

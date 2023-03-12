package com.example.dirverapp.utils

import com.example.dirverapp.BuildConfig
import com.example.dirverapp.other.Constants.BASE_URL

sealed class ApiEnvironment(val baseUrl: String) {
    object Dev : ApiEnvironment(BASE_URL)
    object Staging : ApiEnvironment(BASE_URL)
    object Prod : ApiEnvironment(BASE_URL)
}

fun getEnvironment(): ApiEnvironment {
    val flavor = BuildConfig.FLAVOR

    return when {
        flavor.contains("Dev", true) -> ApiEnvironment.Dev
        flavor.contains("Staging", true) -> ApiEnvironment.Staging
        flavor.contains("Prod", true) -> ApiEnvironment.Prod
        else -> ApiEnvironment.Dev
    }
}

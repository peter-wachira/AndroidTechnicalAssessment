package com.example.dirverapp.utils

import com.example.dirverapp.BuildConfig

sealed class ApiEnvironment(val baseUrl: String) {
    object Dev : ApiEnvironment("https://run.mocky.io")
    object Staging : ApiEnvironment("https://run.mocky.io")
    object Prod : ApiEnvironment("https://run.mocky.io")
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

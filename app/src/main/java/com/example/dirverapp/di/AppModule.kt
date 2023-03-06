package com.example.dirverapp.di

import com.example.dirverapp.data.remote.MockAPI
import com.example.dirverapp.other.Constants.BASE_URL
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

object AppModule {
    @Singleton
    @Provides
    fun provideMockApi(): MockAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(MockAPI::class.java)
    }
}
package com.example.dirverapp.di

import android.content.Context
import androidx.room.Room
import com.example.dirverapp.data.local.OrdersDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {
    @Provides
    @Named("orders_db")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, OrdersDatabase::class.java)
            .allowMainThreadQueries()
            .build()
}

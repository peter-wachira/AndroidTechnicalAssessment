package com.example.dirverapp.di

import android.content.Context
import androidx.room.Room
import com.example.dirverapp.data.local.OrdersDao
import com.example.dirverapp.data.local.OrdersDatabase
import com.example.dirverapp.data.remote.MockAPI
import com.example.dirverapp.other.Constants.BASE_URL
import com.example.dirverapp.other.Constants.DATABASE_NAME
import com.example.dirverapp.ui.list.DeliveriesRepository
import com.example.dirverapp.ui.list.DeliveriesRepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
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

    @Singleton
    @Provides
    fun provideOrdersDao(
        database: OrdersDatabase,
    ) = database.orderDao()

    @Singleton
    @Provides
    fun provideOrdersDataBase(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(context, OrdersDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideOrdersRepository(
        dao: OrdersDao,
        api: MockAPI,
    ) = DeliveriesRepository(dao, api) as DeliveriesRepositoryInterface
}

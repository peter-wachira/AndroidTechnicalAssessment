package com.example.dirverapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dirverapp.data.remote.OrderEntity

@Database(
    entities = [OrderEntity::class],
    version = 6,
    exportSchema = false,
)
abstract class OrdersDatabase : RoomDatabase() {
    abstract fun orderDao(): OrdersDao
}

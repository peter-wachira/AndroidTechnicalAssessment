package com.example.dirverapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dirverapp.data.remote.OrderEntity

@Dao
interface OrdersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(vararg orderEntity: OrderEntity)

    @Query("SELECT * FROM orders")
    suspend fun observeAllOrders(): LiveData<List<OrderEntity>>

    @Delete
    suspend fun deleteOrderItem(orderEntity: OrderEntity)
}

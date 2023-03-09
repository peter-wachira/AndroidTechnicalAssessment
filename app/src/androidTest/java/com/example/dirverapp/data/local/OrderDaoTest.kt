package com.example.dirverapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.dirverapp.data.remote.OrderEntity
import com.example.dirverapp.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
@ExperimentalCoroutinesApi
class OrderDaoTest {
    @get: Rule
    val instantTaskExecutor = InstantTaskExecutorRule()

    private lateinit var database: OrdersDatabase
    private lateinit var ordersDao: OrdersDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            OrdersDatabase::class.java,
        ).allowMainThreadQueries().build()

        ordersDao = database.orderDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertOrderItem() = runBlockingTest {
        val orderItem = OrderEntity("John Smith", "456 Elm St", "67890", "5678", "5678", -1.283333, 36.816667, "Nairobi", "Delivered", "B234", "C789")
        ordersDao.insertOrder(orderItem)
        /*convert livedata item to list */
        val allOrderItems = ordersDao.observeAllOrders().getOrAwaitValue()
        assertThat(allOrderItems.contains(orderItem))
    }

    @Test
    fun deleteShoppingItems() = runBlockingTest {
        val orderItem = OrderEntity("John Smith", "456 Elm St", "67890", "5678", "5678", -1.283333, 36.816667, "Nairobi", "Delivered", "B234", "C789")
        ordersDao.insertOrder(orderItem)
        ordersDao.deleteOrderItem(orderItem)
        val allOrderItems = ordersDao.observeAllOrders().getOrAwaitValue()
        assertThat(allOrderItems).doesNotContain(orderItem)
    }

    @Test
    fun observeAllShoppingItems() = runBlockingTest {
        val orderItem1 = OrderEntity("John Smith", "456 Elm St", "133", "5678", "5678", -1.283333, 36.816667, "Nairobi", "Delivered", "B234", "C789")
        val orderItem2 = OrderEntity("John Smith", "456 Elm St", "144", "5678", "5678", -1.283333, 36.816667, "Nairobi", "Delivered", "B234", "C789")
        val orderItem3 = OrderEntity("John Smith", "456 Elm St", "155", "5678", "5678", -1.283333, 36.816667, "Nairobi", "Delivered", "B234", "C789")
        ordersDao.insertOrder(orderItem1)
        ordersDao.insertOrder(orderItem2)
        ordersDao.insertOrder(orderItem3)

        val totalItemsInDb = ordersDao.observeAllOrders().getOrAwaitValue().size
        assertThat(totalItemsInDb).isEqualTo(3)
    }
}

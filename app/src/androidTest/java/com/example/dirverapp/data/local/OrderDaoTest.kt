package com.example.dirverapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.dirverapp.data.remote.orders.OrderEntity
import com.example.dirverapp.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class OrderDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get: Rule
    val instantTaskExecutor = InstantTaskExecutorRule()

    @Inject
    @Named("orders_db")
    lateinit var database: OrdersDatabase
    private lateinit var ordersDao: OrdersDao

    @Before
    fun setup() {
        hiltRule.inject()
        ordersDao = database.orderDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertOrderItem() = runTest {
        val orderItem = OrderEntity("John Smith", "456 Elm St", "67890", "5678", "5678", -1.283333, 36.816667, "Nairobi", "Delivered", "B234", "C789")
        ordersDao.insertOrder(orderItem)
        /*convert livedata item to list */
        val allOrderItems = ordersDao.observeAllOrders().getOrAwaitValue()
        assertThat(allOrderItems.contains(orderItem))
    }

    @Test
    fun deleteShoppingItems() = runTest {
        val orderItem = OrderEntity("John Smith", "456 Elm St", "67890", "5678", "5678", -1.283333, 36.816667, "Nairobi", "Delivered", "B234", "C789")
        ordersDao.insertOrder(orderItem)
        ordersDao.deleteOrderItem(orderItem)
        val allOrderItems = ordersDao.observeAllOrders().getOrAwaitValue()
        assertThat(allOrderItems).doesNotContain(orderItem)
    }

    @Test
    fun observeAllShoppingItems() = runTest {
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

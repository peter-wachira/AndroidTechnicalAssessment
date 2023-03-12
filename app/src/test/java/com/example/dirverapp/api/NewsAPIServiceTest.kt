package com.example.dirverapp.api // ktlint-disable filename

import com.example.dirverapp.data.remote.MockAPI
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MockAPITest {

    private lateinit var service: MockAPI
    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(server.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MockAPI::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    private fun enqueueMockResponse(
        fileName: String,
    ) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        mockResponse.setBody(source.readString(Charsets.UTF_8))
        server.enqueue(mockResponse)
    }

    @Test
    fun getOrders_sentRequest_receivedExpected() {
        runBlocking {
            enqueueMockResponse("orders_response.json")
            val responseBody = service.getOrders()
            val request = server.takeRequest()
            assertThat(responseBody).isNotNull()
            assertThat(request.path).isEqualTo("/v3/c7bb0f75-a033-48b0-90d2-385111fe15c2")
        }
    }

    @Test
    fun getOrders_receivedResponse_correctSize() {
        runBlocking {
            enqueueMockResponse("orders_response.json")
            val responseBody = service.getOrders()
            val orders = responseBody.orders
            assertThat(orders.size).isEqualTo(7)
        }
    }

    @Test
    fun getOrders_receivedResponse_correctContent() {
        runBlocking {
            enqueueMockResponse("orders_response.json")
            val responseBody = service.getOrders()
            val ordersList = responseBody.orders
            val order = ordersList[0]
            assertThat(order.address).isEqualTo("456 Elm St")
            assertThat(order.orderBatchNumber).isEqualTo("67890")
            assertThat(order.primaryPhone).isEqualTo("555-5678")
        }
    }

    @Test
    fun getSalesAreaDetails_sentRequest_receivedExpected() {
        runBlocking {
            enqueueMockResponse("salesarearesponse.json")
            val responseBody = service.getSalesAreaDetails()
            val request = server.takeRequest()
            assertThat(responseBody).isNotNull()
            assertThat(request.path).isEqualTo("/v3/9b36445d-25d1-4f97-9ac5-4123466ee298")
        }
    }

    @Test
    fun getSalesAreaDetails_receivedResponse_correctSize() {
        runBlocking {
            enqueueMockResponse("salesarearesponse.json")
            val responseBody = service.getSalesAreaDetails()
            val areas = responseBody.areas
            assertThat(areas.size).isEqualTo(5)
        }
    }

    @Test
    fun getSalesAreaDetails_receivedResponse_correctContent() {
        runBlocking {
            enqueueMockResponse("salesarearesponse.json")
            val responseBody = service.getSalesAreaDetails()
            val areasList = responseBody.areas
            val area = areasList[0]
            assertThat(area.salesAreaCode).isEqualTo("SA1")
            assertThat(area.name).isEqualTo("Area 1")
            assertThat(area.territory.type).isEqualTo("Polygon")
        }
    }
}

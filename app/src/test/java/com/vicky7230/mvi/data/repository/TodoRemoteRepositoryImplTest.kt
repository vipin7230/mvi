package com.vicky7230.mvi.data.repository

import com.vicky7230.mvi.Helper
import com.vicky7230.mvi.data.remote.TodoApi
import com.vicky7230.mvi.data.remote.TodoRemoteSource
import com.vicky7230.mvi.data.remote.TodoRemoteSourceImpl
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class TodoRemoteRepositoryImplTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var todoApi: TodoApi
    private lateinit var todoRemoteSource: TodoRemoteSource

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        val contentType = "application/json".toMediaType()
        todoApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build().create(TodoApi::class.java)

        todoRemoteSource = TodoRemoteSourceImpl(todoApi)
    }

    @Test(expected = HttpException::class)
    fun testGetTodos_returnsException() = runTest {

        val mockResponse = MockResponse()
        mockResponse.setBody("[]")
        mockResponse.setResponseCode(500)
        mockWebServer.enqueue(mockResponse)

        todoRemoteSource.getTodos()
    }

    @Test
    fun testGetTodos_returnTodos() = runTest {

        val mockResponse = MockResponse()

        val content = Helper.readFileResource("mock-response/response.json")

        mockResponse.setBody(content)
        mockResponse.setResponseCode(200)
        mockWebServer.enqueue(mockResponse)

        val response = todoRemoteSource.getTodos()
        mockWebServer.takeRequest()

        Assert.assertEquals(false, response.isEmpty())
        Assert.assertEquals(200, response.size)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

}

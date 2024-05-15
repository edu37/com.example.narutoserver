package com.example.routes

import com.example.di.koinModule
import com.example.plugins.configureRouting
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Rule
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import kotlin.test.Test
import kotlin.test.assertEquals

class RouteEndpointTest : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(koinModule)
    }

    @Test
    fun `Given root endpoint, When access correctly, Then get right response`() = testApplication {
        application {
            configureRouting()
        }
        client.get("/").apply {
            assertEquals(
                expected = HttpStatusCode.OK,
                actual = status
            )
            assertEquals(
                expected = "Welcome to our Api!",
                actual = body()
            )
        }
    }
}

package com.example.routes

import com.example.di.koinModule
import com.example.model.ApiResponse
import com.example.plugins.configureRouting
import com.example.repository.HeroRepository
import com.example.util.ExceptionHandler
import com.example.util.calculateNextPage
import com.example.util.calculatePreviousPage
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.junit.Rule
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAllHeroesEndpointTest : KoinTest {
    private val repository: HeroRepository by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(koinModule)
    }

    @Test
    fun `Given allHeroes endpoint, When access any page correctly, Then get the right page of heroes`() =
        testApplication {
            application {
                configureRouting()
            }
            val pages = 1..repository.heroes.size

            pages.forEach { page ->
                client.get("/naruto/heroes?page=$page").apply {
                    assertEquals(
                        expected = HttpStatusCode.OK,
                        actual = status
                    )

                    val expectedResponse = ApiResponse(
                        success = true,
                        message = "getAllHeroes",
                        previousPage = page.calculatePreviousPage(),
                        nextPage = page.calculateNextPage(),
                        heroes = repository.heroes[page]!!
                    )

                    val actualResponse = mapToApiResponse(bodyAsText())

                    assertEquals(
                        expected = expectedResponse,
                        actual = actualResponse
                    )
                }
            }
        }

    @Test
    fun `Given allHeroes endpoint, When access without query, Then get a page1 of heroes`() = testApplication {
        application {
            configureRouting()
        }
        client.get("/naruto/heroes").apply {
            assertEquals(
                expected = HttpStatusCode.OK,
                actual = status
            )

            val expectedResponse = ApiResponse(
                success = true,
                message = "getAllHeroes",
                previousPage = null,
                nextPage = 2,
                heroes = repository.page1
            )

            val actualResponse = mapToApiResponse(bodyAsText())

            assertEquals(
                expected = expectedResponse,
                actual = actualResponse
            )
        }
    }

    @Test
    fun `Given allHeroes endpoint, When type a non existing page, Then throw a 404 error not found`() =
        testApplication {
            application {
                configureRouting()
            }
            client.get("/naruto/heroes?page=234").apply {
                assertEquals(
                    expected = HttpStatusCode.NotFound,
                    actual = status
                )

                val expectedResponse = ExceptionHandler.exceptionResponse("Page not found.")

                val actualResponse = mapToApiResponse(bodyAsText())

                assertEquals(
                    expected = expectedResponse,
                    actual = actualResponse
                )
            }
        }

    @Test
    fun `Given allHeroes endpoint, When type a invalid page, Then throw a 400 error bad request`() =
        testApplication {
            application {
                configureRouting()
            }
            client.get("/naruto/heroes?page=naruto").apply {
                assertEquals(
                    expected = HttpStatusCode.BadRequest,
                    actual = status
                )

                val expectedResponse = ExceptionHandler.exceptionResponse("Only numbers allowed.")

                val actualResponse = mapToApiResponse(bodyAsText())

                assertEquals(
                    expected = expectedResponse,
                    actual = actualResponse
                )
            }
        }

    private fun mapToApiResponse(json: String) = Json.decodeFromString<ApiResponse>(json)
}
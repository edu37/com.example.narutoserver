package com.example.routes

import com.example.di.koinModule
import com.example.model.ApiResponse
import com.example.model.Hero
import com.example.plugins.configureRouting
import com.example.repository.Heroes
import com.example.util.ExceptionHandler
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.junit.Rule
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import kotlin.test.Test
import kotlin.test.assertEquals

class SearchHeroEndpointTest : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(koinModule)
    }

    @Test
    fun `Given searchHero endpoint, When query match one or more heroes, Then return a list of heroes`() =
        testApplication {
            application {
                configureRouting()
            }
            client.get("/naruto/heroes/search?name=ruto").apply {
                assertEquals(
                    expected = HttpStatusCode.OK,
                    actual = status
                )
                val matchedHeroes = listOf(Heroes.Naruto, Heroes.Boruto)

                val expectedResponse = ApiResponse(
                    success = true,
                    message = "searchHero",
                    heroes = matchedHeroes
                )

                val actualResponse = mapToApiResponse(bodyAsText())

                assertEquals(
                    expected = expectedResponse,
                    actual = actualResponse
                )
            }
        }

    @Test
    fun `Given searchHero endpoint, When query doesn't match any hero, Then throw a 404 error not found`() =
        testApplication {
            application {
                configureRouting()
            }
            client.get("/naruto/heroes/search?name=gaara").apply {
                assertEquals(
                    expected = HttpStatusCode.NotFound,
                    actual = status
                )
                val matchedHeroes = emptyList<Hero>()

                val expectedResponse = ApiResponse(
                    success = false,
                    message = "No heroes found.",
                    heroes = matchedHeroes
                )

                val actualResponse = mapToApiResponse(bodyAsText())

                assertEquals(
                    expected = expectedResponse,
                    actual = actualResponse
                )
            }
        }

    @Test
    fun `Given searchHero endpoint, When query is empty, Then throw a 404 error not found`() =
        testApplication {
            application {
                configureRouting()
            }
            client.get("/naruto/heroes/search").apply {
                assertEquals(
                    expected = HttpStatusCode.NotFound,
                    actual = status
                )

                val expectedResponse = ExceptionHandler.exceptionResponse("No heroes found.")

                val actualResponse = mapToApiResponse(bodyAsText())

                assertEquals(
                    expected = expectedResponse,
                    actual = actualResponse
                )
            }
        }

    private fun mapToApiResponse(json: String) = Json.decodeFromString<ApiResponse>(json)
}
package com.example.routes

import com.example.repository.HeroRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.searchHero() {
    val heroRepository: HeroRepository by inject()

    get("naruto/heroes/search") {
        try {
            val name = call.request.queryParameters["name"]

            val response = heroRepository.searchHero(name)

            call.respond(
                message = response,
                status = HttpStatusCode.OK
            )

        } catch (e: Exception) {

        }
    }
}
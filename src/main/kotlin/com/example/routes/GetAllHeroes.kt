package com.example.routes

import com.example.repository.HeroRepository
import com.example.util.ExceptionHandler
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.getAllHeroes() {
    val heroRepository: HeroRepository by inject()

    get("naruto/heroes") {
        try {
            val page = call.request.queryParameters["page"]?.toInt() ?: 1
            require(page in 1..5)

            val response = heroRepository.getAllHeroes(page)

            call.respond(
                message = response,
                status = HttpStatusCode.OK
            )
        } catch (exception: NumberFormatException) {
            val response = ExceptionHandler.exceptionResponse("Only numbers allowed.")
            call.respond(
                message = response,
                status = HttpStatusCode.BadRequest
            )
        } catch (exception: IllegalArgumentException) {
            val response = ExceptionHandler.exceptionResponse("Page not found.")
            call.respond(
                message = response,
                status = HttpStatusCode.NotFound
            )
        }
    }
}
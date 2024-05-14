package com.example.routes

import com.example.model.ApiResponse
import com.example.repository.HeroRepository
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
            call.respond(
                message = exceptionResponse("Only numbers allowed."),
                status = HttpStatusCode.BadRequest
            )
        } catch (exception: IllegalArgumentException) {
            call.respond(
                message = exceptionResponse("Heroes not found."),
                status = HttpStatusCode.NotFound
            )
        }
    }
}

private fun exceptionResponse(message: String): ApiResponse {
    return ApiResponse(
        success = false,
        message = message
    )
}
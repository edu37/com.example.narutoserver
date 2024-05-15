package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*

fun Application.configureStatusPages() {
    install(StatusPages) {
//        status(HttpStatusCode.NotFound) { call, status ->
//            call.respond(
//                message = "404 Error: Page not found.",
//                status = status
//            )
//        }
    }
}
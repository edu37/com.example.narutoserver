package com.example.util

import com.example.model.ApiResponse

object ExceptionHandler {

    fun exceptionResponse(message: String): ApiResponse {
        return ApiResponse(
            success = false,
            message = message
        )
    }
}
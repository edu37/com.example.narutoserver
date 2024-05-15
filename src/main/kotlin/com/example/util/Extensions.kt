package com.example.util

fun Int.calculatePreviousPage(): Int? {
    return when (this) {
        in 2..5 -> minus(1)
        else -> null
    }
}

fun Int.calculateNextPage(): Int? {
    return when (this) {
        in 1..4 -> plus(1)
        else -> null
    }
}
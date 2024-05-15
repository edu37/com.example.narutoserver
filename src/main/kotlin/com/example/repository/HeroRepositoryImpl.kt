package com.example.repository

import com.example.model.ApiResponse
import com.example.model.Hero
import com.example.util.calculateNextPage
import com.example.util.calculatePreviousPage

class HeroRepositoryImpl : HeroRepository {
    override val heroes: Map<Int, List<Hero>> by lazy {
        mapOf(
            1 to page1,
            2 to page2,
            3 to page3,
            4 to page4,
            5 to page5
        )
    }

    override val page1 = listOf(
        Heroes.Naruto,
        Heroes.Sasuke,
        Heroes.Sakura
    )
    override val page2 = listOf(
        Heroes.Boruto,
        Heroes.Sarada,
        Heroes.Mitsuki
    )
    override val page3 = listOf(
        Heroes.Kawaki,
        Heroes.Kakashi,
        Heroes.Orochimaru
    )
    override val page4 = listOf(
        Heroes.Isshiki,
        Heroes.Momoshiki,
        Heroes.Urashiki,
    )
    override val page5 = listOf(
        Heroes.Code,
        Heroes.Amado,
        Heroes.Koki,
    )

    override suspend fun getAllHeroes(page: Int): ApiResponse {
        return ApiResponse(
            success = true,
            message = "getAllHeroes",
            previousPage = page.calculatePreviousPage(),
            nextPage = page.calculateNextPage(),
            heroes = heroes[page]!!
        )
    }

    override suspend fun searchHero(name: String?): ApiResponse {
        return ApiResponse(
            success = true,
            message = "searchHero",
            heroes = findHeroOnLists(name)
        )
    }

    private fun findHeroOnLists(query: String?): List<Hero> {
        return if (query.isNullOrEmpty()) {
            emptyList()
        } else {
            val matchedHeroes = mutableListOf<Hero>()
            for (page in 1..5) {
                val heroesOnPage = heroes[page] ?: continue
                heroesOnPage.forEach { hero ->
                    if (hero.name.lowercase().contains(query))
                        matchedHeroes.add(hero)
                }
            }
            matchedHeroes.toList()
        }
    }

}
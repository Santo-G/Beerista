package com.worldbeers.beerista.domain

interface BeerAPI {
    suspend fun loadBeers(beers: String): LoadBeersResult

    suspend fun loadDetailBeer(idBeer: Unit): LoadDetailBeer
}

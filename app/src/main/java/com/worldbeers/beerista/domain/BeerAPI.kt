package com.worldbeers.beerista.domain

interface BeerAPI {
    suspend fun loadBeers(): LoadBeersResult
    suspend fun loadDetailBeer(idBeer: Int): LoadBeersResult
}

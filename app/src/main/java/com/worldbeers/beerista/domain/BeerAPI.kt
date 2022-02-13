package com.worldbeers.beerista.domain

interface BeerAPI {
    suspend fun loadBeers(): LoadBeersResult
    suspend fun loadDetailBeer(idBeer: Int): LoadBeersResult
    suspend fun loadSearchBeers(brewedAfter: String, brewedBefore : String) : LoadBeersResult
    suspend fun loadScrollBeers(pageNum : Int) : LoadBeersResult
}

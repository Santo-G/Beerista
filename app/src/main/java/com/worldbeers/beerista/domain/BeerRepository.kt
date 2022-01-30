package com.worldbeers.beerista.domain



interface BeerRepository {
    suspend fun loadBeers(): LoadBeersResult
}


// previous interface implementation
class BeerRepositoryImpl(
    private val api: BeerAPI,
) : BeerRepository {
    override suspend fun loadBeers(): LoadBeersResult {
        return api.loadBeers()
    }
}

package com.worldbeers.beerista.domain


interface BeerRepository {
    suspend fun loadBeers(): LoadBeersResult
    suspend fun loadSearchBeers(brewedAfter : String, brewedBefore : String): LoadBeersResult
    suspend fun loadScrollBeers(pageNum : Int) : LoadBeersResult
}


// previous interface implementation
class BeerRepositoryImpl(
    private val api: BeerAPI,
) : BeerRepository {

    override suspend fun loadBeers(): LoadBeersResult {
        return api.loadBeers()
    }

    override suspend fun loadSearchBeers(brewedAfter: String, brewedBefore: String): LoadBeersResult {
        return api.loadSearchBeers(brewedAfter,brewedBefore)
    }

    override suspend fun loadScrollBeers(pageNum : Int): LoadBeersResult {
        return api.loadScrollBeers(pageNum)
    }
}

package com.worldbeers.beerista.domain

sealed class LoadBeersResult {
    data class Success(val beers: ArrayList<Beer>) : LoadBeersResult()
    data class Failure(val error: LoadBeersError) : LoadBeersResult()
}

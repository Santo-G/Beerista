package com.worldbeers.beerista.domain

sealed class LoadBeersError {
    object NoBeersFound : LoadBeersError()
    object ServerError : LoadBeersError()
}

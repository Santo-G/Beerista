package com.worldbeers.beerista.domain

sealed class DetailLoadBeerError {
    object ServerError : DetailLoadBeerError()
    object NoDetailFound : DetailLoadBeerError()
}

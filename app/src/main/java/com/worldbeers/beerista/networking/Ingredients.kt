package com.worldbeers.beerista.networking

data class Ingredients(
    val hops: List<Any>,
    val malt: List<Malt>,
    val yeast: String
)

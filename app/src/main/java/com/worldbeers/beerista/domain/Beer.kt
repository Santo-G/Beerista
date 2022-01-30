package com.worldbeers.beerista.domain

data class Beer(
    val idBeer: Int,
    val name: String,
    val image: String,
    val description: String,
    val abv: Double?,
    val ibu: Double?
)

package com.worldbeers.beerista.domain

import java.io.Serializable

data class Beer(
    val idBeer: Int,
    val name: String,
    val image: String,
    val description: String,
    val abv: Double?,
    val ibu: Double?,
    val first_brewed: String,
    val food_pairing: List<String>,
    val tagline: String
) : Serializable

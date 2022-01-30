package com.worldbeers.beerista.networking

data class Method(
    val fermentation: Fermentation,
    val mash_temp: List<MashTemp>,
    val twist: Any
)

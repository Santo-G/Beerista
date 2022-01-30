package com.worldbeers.beerista.networking

import retrofit2.http.GET
import retrofit2.http.Query

public interface BeerService {

    @GET("beers" )
    suspend fun loadBeers(@Query("b")b: String): BeerDTO

    @GET("beer")
    suspend fun loadSingleBeer(@Query("id")id: Unit): BeerDTO.BeerDTOItem

}

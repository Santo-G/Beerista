package com.worldbeers.beerista.networking

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

public interface BeerService {

    @GET("beers")
    suspend fun loadBeers(): BeerDTO

    /*
    @GET("beers/{id}")
    suspend fun loadSingleBeer(@Path("id") id: Int): BeerDTO.BeerDTOItem
    */

}

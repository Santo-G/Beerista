package com.worldbeers.beerista.networking

import retrofit2.http.GET
import retrofit2.http.Query

public interface BeerService {

    @GET("beers")
    suspend fun loadBeers(): BeerDTO

    /*
    @GET("beers/{id}")
    suspend fun loadSingleBeer(@Path("id") id: Int): BeerDTO.BeerDTOItem
    */

    @GET("beers")
    suspend fun loadSearchBeers(@Query("brewed_after") brewed_after: String, @Query("brewed_before") brewed_before: String): BeerDTO

    @GET("beers")
    suspend fun loadScrollBeers(@Query("page") page: Int) : BeerDTO

}

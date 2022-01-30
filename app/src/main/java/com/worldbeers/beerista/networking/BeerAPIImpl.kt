package com.worldbeers.beerista.networking

import com.worldbeers.beerista.domain.Beer
import com.worldbeers.beerista.domain.BeerAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException

class BeerAPIImpl : BeerAPI {

    private val service: BeerService

    init {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.punkapi.com/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        service = retrofit.create(BeerService::class.java)
    }

    override suspend fun loadBeers(beers: String): LoadBeersResult {
        // try-catch per gestire errore chiamata di rete
        try {
            val beersList = service.loadBeers(beers)
            val beers = beersList.beers.mapNotNull {
                it.toDomain()
            }


            return if (cocktails.isEmpty()) {
                Failure(NoCocktailFound)
            } else {
                Success(cocktails)
            }
        } catch (e: IOException) { // no internet
            return Failure(NoInternet)
        } catch (e: SocketTimeoutException) {
            return Failure(SlowInternet)
        } catch (e: Exception) {
            Timber.e(e, "Generic Exception on LoadCocktail")
            return Failure(ServerError)
        }
    }

    override suspend fun loadDetailBeer(idBeer: Unit): LoadDetailBeer {
        TODO("Not yet implemented")
    }

    private fun BeerDTO.BeerDTOItem.toDomain(): Beer? {
        val id = id_beer.toLongOrNull()
        return if (id != null) {
            Beer(
                idBeer = id.toInt(),
                name = str_name,
                image = str_image_url,
                description = str_description,
                abv = dbl_abv,
                ibu = dbl_ibu
            )
        } else {
            null
        }
    }
}

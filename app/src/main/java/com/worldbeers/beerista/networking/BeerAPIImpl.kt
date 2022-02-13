package com.worldbeers.beerista.networking

import android.util.Log
import com.worldbeers.beerista.domain.Beer
import com.worldbeers.beerista.domain.BeerAPI
import com.worldbeers.beerista.domain.LoadBeersError
import com.worldbeers.beerista.domain.LoadBeersResult
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

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

    override suspend fun loadBeers(): LoadBeersResult {
        // try-catch for networking error management
        try {
            val beersList = service.loadBeers()
            val beers = beersList.mapNotNull {
                it.toDomain()
            }
            return if (beers.isEmpty()) {
                LoadBeersResult.Failure(LoadBeersError.NoBeersFound)
            } else {
                LoadBeersResult.Success(beers as ArrayList<Beer>)
            }
        } catch (e: Exception) {
            Log.d("Exception", e.message.toString())
            Timber.e(e, "Generic Exception on LoadBeers")
            return LoadBeersResult.Failure(LoadBeersError.ServerError)
        }
    }

    override suspend fun loadDetailBeer(idBeer: Int): LoadBeersResult {
        // TODO not implemented because if we make another call for other details
        //  and internet is down probably user won't have a detail of beers
        return LoadBeersResult.Failure(LoadBeersError.NoBeersFound)
    }

    override suspend fun loadSearchBeers(brewedAfter: String, brewedBefore: String) : LoadBeersResult {
        // try-catch for networking error management
        try {
            val beersSearchList = service.loadSearchBeers(brewedAfter, brewedBefore)
            val beers = beersSearchList.mapNotNull {
                it.toDomain()
            }
            return if (beers.isEmpty()) {
                LoadBeersResult.Failure(LoadBeersError.NoBeersFound)
            } else {
                LoadBeersResult.Success(beers as ArrayList<Beer>)
            }
        } catch (e: Exception) {
            Log.d("Exception", e.message.toString())
            Timber.e(e, "Generic Exception on LoadBeers")
            return LoadBeersResult.Failure(LoadBeersError.ServerError)
        }
    }

    override suspend fun loadScrollBeers(pageNum: Int): LoadBeersResult {
        // try-catch for networking error management
        try {
            val beersScrollList = service.loadScrollBeers(pageNum)
            val beers = beersScrollList.mapNotNull {
                it.toDomain()
            }
            return if (beers.isEmpty()) {
                LoadBeersResult.Failure(LoadBeersError.NoBeersFound)
            } else {
                LoadBeersResult.Success(beers as ArrayList<Beer>)
            }
        } catch (e: Exception) {
            Log.d("Exception", e.message.toString())
            Timber.e(e, "Generic Exception on LoadBeers")
            return LoadBeersResult.Failure(LoadBeersError.ServerError)
        }
    }


    private fun BeerDTOItem.toDomain(): Beer? {
        val id = id
        return if (id != null) {
            Beer(
                idBeer = id.toInt(),
                name = name,
                image = image_url,
                description = description,
                abv = abv,
                ibu = ibu,
                first_brewed = first_brewed,
                food_pairing = food_pairing,
                brewers_tips = brewers_tips,
            )
        } else {
            null
        }
    }
}

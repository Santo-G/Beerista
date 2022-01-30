package com.worldbeers.beerista.koindi

import com.worldbeers.beerista.domain.BeerAPI
import com.worldbeers.beerista.networking.BeerAPIImpl
import org.koin.dsl.module

val networkingKoinModule = module {
    single<BeerAPI> {
        BeerAPIImpl()
    }
}

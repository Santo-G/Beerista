package com.worldbeers.beerista.domain

import com.worldbeers.beerista.home.HomeViewModel
import com.worldbeers.beerista.home.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

// declaring all the dependencies that will be injected in other components

val appModule = module {
    /* creating a Singleton Pattern repository */
    single<BeerRepository> {
        BeerRepositoryImpl(api = get())
    }

    // creating ViewModel objects
    viewModel { HomeViewModel(repository = get()) }
    viewModel { SearchViewModel(repository = get()) }

}

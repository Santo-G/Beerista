package com.worldbeers.beerista.domain

import com.worldbeers.beerista.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

// declaring all the dependencies that will be injected in another components

val appModule = module {
    /* creiamo un repository Singleton Pattern */
    single<BeerRepository> {
        BeerRepositoryImpl(api = get())
    }


    // creating ViewModel objects
    viewModel { HomeViewModel(repository = get()) }
    /*viewModel { SearchViewModel(repository = get(), tracking = get()) }
    viewModel { MainActivityViewModel(settingsrepository = get(), tracking = get()) }*/

}

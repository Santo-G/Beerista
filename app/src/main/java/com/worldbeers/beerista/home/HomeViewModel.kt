package com.worldbeers.beerista.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.worldbeers.beerista.domain.Beer
import com.worldbeers.beerista.domain.BeerRepository
import com.worldbeers.beerista.domain.LoadBeersError
import com.worldbeers.beerista.domain.LoadBeersResult
import com.worldbeers.beerista.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel(
    private val repository: BeerRepository,

    ) : ViewModel() {

    val states = MutableLiveData<HomeScreenStates>()
    val actions = SingleLiveEvent<HomeScreenActions>()

    // Check the type of event and acts accordingly
    fun send(event: HomeScreenEvents) {
        Timber.d(event.toString())
        when (event) {
            HomeScreenEvents.OnReady -> {
                loadContent()
            }
            is HomeScreenEvents.OnBeerClick -> {
                actions.postValue(HomeScreenActions.NavigateToDetail(event.beer))
            }
            is HomeScreenEvents.OnRefreshClicked -> {
                loadContent()
            }
            is HomeScreenEvents.OnSearch -> {
                actions.postValue(HomeScreenActions.NavigateToSearch(event.from, event.to))
            }
            is HomeScreenEvents.OnScroll -> {
                loadScroll(event.pageNum)
            }
        }
    }

    private fun loadContent() {
        states.postValue(HomeScreenStates.Loading)
        viewModelScope.launch {
            val beerResult = repository.loadBeers()
            when (beerResult) {
                is LoadBeersResult.Failure -> onBeersFailure(beerResult)
                is LoadBeersResult.Success -> {
                    val beers = beerResult.beers.map {
                        Beer(
                            name = it.name,
                            image = it.image,
                            idBeer = it.idBeer,
                            description = it.description,
                            abv = it.abv,
                            ibu = it.ibu,
                            first_brewed = it.first_brewed,
                            food_pairing = it.food_pairing,
                            tagline = it.tagline
                        )
                    }
                    states.postValue(HomeScreenStates.Content(beers))
                }
            }
        }
    }

    private fun loadSearch(from: String, to: String) {
        states.postValue(HomeScreenStates.Loading)
        viewModelScope.launch {
            val beerResult = repository.loadSearchBeers(from, to)
            when (beerResult) {
                is LoadBeersResult.Failure -> onBeersFailure(beerResult)
                is LoadBeersResult.Success -> {
                    val beers = beerResult.beers.map {
                        Beer(
                            name = it.name,
                            image = it.image,
                            idBeer = it.idBeer,
                            description = it.description,
                            abv = it.abv,
                            ibu = it.ibu,
                            first_brewed = it.first_brewed,
                            food_pairing = it.food_pairing,
                            tagline = it.tagline
                        )
                    }
                    states.postValue(HomeScreenStates.Content(beers))
                }
            }
        }
    }

    private fun loadScroll(pageNum: Int) {
        states.postValue(HomeScreenStates.Loading)
        viewModelScope.launch {
            val beerResult = repository.loadScrollBeers(pageNum)
            when (beerResult) {
                is LoadBeersResult.Failure -> onBeersFailure(beerResult)
                is LoadBeersResult.Success -> {
                    val beers = beerResult.beers.map {
                        Beer(
                            name = it.name,
                            image = it.image,
                            idBeer = it.idBeer,
                            description = it.description,
                            abv = it.abv,
                            ibu = it.ibu,
                            first_brewed = it.first_brewed,
                            food_pairing = it.food_pairing,
                            tagline = it.tagline
                        )
                    }
                    states.postValue(HomeScreenStates.Content(beers))
                }
            }
        }
    }

    private fun onBeersFailure(result: LoadBeersResult.Failure) {
        when (result.error) {
            LoadBeersError.NoBeersFound -> states.postValue(HomeScreenStates.Error(ErrorStates.ShowNoBeerFound))
            LoadBeersError.ServerError -> states.postValue(HomeScreenStates.Error(ErrorStates.ShowServerError))
        }
    }
}


/**
 * Contains all objects that represents the state in which the actual screen can be found
 * Loading, Error, Content
 */
sealed class HomeScreenStates {
    object Loading : HomeScreenStates()
    data class Error(val error: ErrorStates) : HomeScreenStates()
    data class Content(val beerList: List<Beer>) : HomeScreenStates()
}

sealed class ErrorStates {
    object ShowNoBeerFound : ErrorStates()
    object ShowServerError : ErrorStates()
}

sealed class HomeScreenActions {
    data class NavigateToDetail(val beer: Beer) : HomeScreenActions()
    class NavigateToSearch(val from: String?, val to: String?) : HomeScreenActions()
}

// Contains all events that can be sent to ViewModel
sealed class HomeScreenEvents {
    data class OnBeerClick(val beer: Beer) : HomeScreenEvents()
    class OnSearch(val from: String?, val to: String?) : HomeScreenEvents()
    object OnReady : HomeScreenEvents()
    object OnRefreshClicked : HomeScreenEvents()
    class OnScroll(val pageNum: Int) : HomeScreenEvents()
}

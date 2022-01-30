package com.worldbeers.beerista.home

import android.os.Bundle
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

    // Check the type of event and acts according to this
    fun send(event: HomeScreenEvents) {
        Timber.d(event.toString())
        when (event) {
            // activity is ready
            HomeScreenEvents.OnReady -> {
                loadContent()
            }
            is HomeScreenEvents.OnBeerClick -> {
                actions.postValue(HomeScreenActions.NavigateToDetail(event.beer))
            }
            is HomeScreenEvents.OnRefreshClicked -> {
                loadContent()
            }
        }
    }

    private fun onBeersFailure(result: LoadBeersResult.Failure) {
        when (result.error) {
            LoadBeersError.NoBeersFound -> states.postValue(HomeScreenStates.Error(ErrorStates.ShowNoBeerFound))
            LoadBeersError.ServerError -> states.postValue(HomeScreenStates.Error(ErrorStates.ShowServerError))
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
                            brewers_tips = it.brewers_tips
                        )
                    }
                    val generalContent = GeneralContent(beers)
                    states.postValue(HomeScreenStates.Content(generalContent))
                }
            }
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
    data class Content(val generalContent: GeneralContent) : HomeScreenStates()
}

sealed class ErrorStates {
    object ShowNoBeerFound : ErrorStates()
    object ShowServerError : ErrorStates()
}

class GeneralContent(val beersList: List<Beer>)


sealed class HomeScreenActions {
    data class NavigateToDetail(val beer: Beer) : HomeScreenActions()
}


// Contains all events that can be sent to ViewModel
sealed class HomeScreenEvents {
    data class OnBeerClick(val beer: Beer) : HomeScreenEvents()
    object OnReady : HomeScreenEvents()
    object OnRefreshClicked : HomeScreenEvents()
}

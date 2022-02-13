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

class SearchViewModel(
    private val repository: BeerRepository
) : ViewModel() {

    val states = MutableLiveData<SearchScreenStates>()
    val actions = SingleLiveEvent<SearchScreenActions>()

    // Check the type of event and acts according to this
    fun send(event: SearchScreenEvents) {
        Timber.d(event.toString())
        when (event) {
            is SearchScreenEvents.OnReady -> {
                loadSearch(event.from, event.to)
            }
            is SearchScreenEvents.OnBeerClick -> {
                actions.postValue(SearchScreenActions.NavigateToDetail(event.beer))
            }
        }
    }

    private fun loadSearch(from: String, to: String) {
        states.postValue(SearchScreenStates.Loading)
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
                            brewers_tips = it.brewers_tips
                        )
                    }
                    states.postValue(SearchScreenStates.Content(beers))
                }
            }
        }
    }

    private fun onBeersFailure(result: LoadBeersResult.Failure) {
        when (result.error) {
            LoadBeersError.NoBeersFound -> states.postValue(SearchScreenStates.Error(ErrorStates.ShowNoBeerFound))
            LoadBeersError.ServerError -> states.postValue(SearchScreenStates.Error(ErrorStates.ShowServerError))
        }
    }
}


/**
 * Contains all objects that represents the state in which the actual screen can be found
 * Loading, Error, Content
 */
sealed class SearchScreenStates {
    object Loading : SearchScreenStates()
    data class Error(val error: ErrorStates) : SearchScreenStates()
    data class Content(val beerList: List<Beer>) : SearchScreenStates()
}

sealed class ErrorSearchStates {
    object ShowNoBeerFound : ErrorStates()
    object ShowServerError : ErrorStates()
}

// Contains all events that can be sent to ViewModel
sealed class SearchScreenEvents {
    data class OnBeerClick(val beer: Beer) : SearchScreenEvents()
    data class OnReady(val from: String, val to: String) : SearchScreenEvents()
}

sealed class SearchScreenActions {
    data class NavigateToDetail(val beer: Beer) : SearchScreenActions()
}


package com.example.aviasales.ui.screen.query

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.aviasales.AviasalesApplication
import com.example.aviasales.data.AppContainer
import com.example.aviasales.data.FavoriteFlight
import com.example.aviasales.data.FlightsRepository
import com.example.aviasales.ui.screen.HomeUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class FavoriteViewModel ( private val repositoryFlight: FlightsRepository): ViewModel(){





    val favoriteFlights: StateFlow<HomeUiState> = repositoryFlight.getFavoriteFlights().map {
        HomeUiState(favoriteFlight = it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState()
    )

    fun addFavoriteFlight(favoriteFlight: FavoriteFlight){
        viewModelScope.launch {
            repositoryFlight.addFavoriteFlight(favoriteFlight)
        }
    }

    fun deleteFavoriteFlight(favoriteFlight: FavoriteFlight){
        viewModelScope.launch {

            repositoryFlight.deleteFavoriteFlight(favoriteFlight)
        }
    }

    companion object{
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = ( this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as
                        AviasalesApplication)
                val rep = application.container.flightsRepository
                FavoriteViewModel( rep)
            }
        }
    }
}
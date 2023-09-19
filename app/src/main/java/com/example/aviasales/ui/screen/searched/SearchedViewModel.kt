package com.example.aviasales.ui.screen.searched

import android.text.Editable.Factory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.aviasales.AviasalesApplication
import com.example.aviasales.data.FavoriteFlight
import com.example.aviasales.data.Flight
import com.example.aviasales.data.FlightsRepository
import com.example.aviasales.ui.screen.HomeUiState
import com.example.aviasales.ui.screen.query.FavoriteViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class SearchedViewModel(savedStateHandle: SavedStateHandle, private val repository: FlightsRepository): ViewModel() {

    private val iataCode: String = checkNotNull(savedStateHandle["iataCode"])

    fun suggestedAirport(): StateFlow<HomeUiState> = repository.getOthers(name = iataCode)
        .map { HomeUiState(suggestedAirport = it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            HomeUiState()
        )

    fun addFavoriteFlight(favoriteFlight: FavoriteFlight){
        viewModelScope.launch {
            repository.addFavoriteFlight(favoriteFlight)
        }
    }

    fun deleteFavoriteFlight(favoriteFlight: FavoriteFlight){
        viewModelScope.launch {
            repository.deleteFavoriteFlight(favoriteFlight)
        }
    }


    val getSelectedAirport: StateFlow<Flight> = repository.getSearched(iataCode)
        .map { Flight(id = it.id, name =  it.name, iataCode = it.iataCode,
                    passengers = it.passengers) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Flight(1,"d", "f", 2)
        )

    companion object{
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = ( this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as
                        AviasalesApplication)
                val rep = application.container.flightsRepository
                SearchedViewModel(this.createSavedStateHandle(), rep)
            }
        }
    }

}
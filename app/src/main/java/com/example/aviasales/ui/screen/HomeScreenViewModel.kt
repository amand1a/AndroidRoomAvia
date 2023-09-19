package com.example.aviasales.ui.screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.util.query
import com.example.aviasales.AviasalesApplication
import com.example.aviasales.data.FavoriteFlight
import com.example.aviasales.data.Flight
import com.example.aviasales.data.FlightsRepository
import com.example.aviasales.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch



data class HomeUiState (
    val listOfAiroports: List<Flight> = listOf(),
    val suggestedAirport: List<Flight> = mutableListOf(),
    val favoriteFlight: List<FavoriteFlight> = mutableListOf(),
    val query: String = "",
    val isHomeScreen: Boolean = true,
    val selectAirport: Flight? = null
        )


class HomeScreenViewModel(private val flightsRepository: FlightsRepository,
    private val userRepository: UserRepository): ViewModel() {

        val uiState  = MutableStateFlow(HomeUiState())

        fun airports(): StateFlow<HomeUiState> =  flightsRepository.searchAirport(uiState.value.query).map {
            HomeUiState(listOfAiroports = it)
        }.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState()
        )

        init {
            updateQuery("")
        }


        fun suggestedAirport(): StateFlow<HomeUiState> = flightsRepository.getOthers(name = uiState.value.selectAirport!!.name)
            .map { HomeUiState(suggestedAirport = it) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                HomeUiState()
            )

        fun addFavoriteFlight(favoriteFlight: FavoriteFlight){
            viewModelScope.launch {
                flightsRepository.addFavoriteFlight(favoriteFlight)
            }
        }

        fun deleteFavoriteFlight(favoriteFlight: FavoriteFlight){
            viewModelScope.launch {
                flightsRepository.deleteFavoriteFlight(favoriteFlight)
            }
        }

        fun changeQuery(query: String){
            viewModelScope.launch {
                userRepository.updateQuery(query)
            }
        }

        fun updateSelectedAirport(flight: Flight){
            uiState.update {
                it.copy(
                    selectAirport = flight
                )
            }
        }

        fun updateQuery(query: String){
            uiState.update {
                it.copy(
                    query = query
                )
            }
        }


        companion object{
            val factory: ViewModelProvider.Factory = viewModelFactory {

                initializer {
                    val application = (this [ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as
                            AviasalesApplication)

                    HomeScreenViewModel(application.container.flightsRepository, application.userRepository)
                }
            }
        }
}
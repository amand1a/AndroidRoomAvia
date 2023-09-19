package com.example.aviasales.data

import kotlinx.coroutines.flow.Flow

interface FlightsRepository {
    fun searchAirport(name: String): Flow<List<Flight>>

    fun getFromIataCode(code: String): Flow<List<Flight>>

    fun getFavoriteFlights(): Flow<List<FavoriteFlight>>

    suspend fun addFavoriteFlight(favoriteFlight: FavoriteFlight)

    suspend fun deleteFavoriteFlight(favoriteFlight: FavoriteFlight)

    fun getSearched(name: String):Flow<Flight>

    fun getOthers(name: String): Flow<List<Flight>>
}

class OfflineFlightRepository(private val flightsDao: FlightsDao): FlightsRepository {

    override fun getFromIataCode(code: String): Flow<List<Flight>> = flightsDao.getFromIataCode(code)

    override fun searchAirport(name: String): Flow<List<Flight>> = flightsDao.searchAirport(name)

    override fun getFavoriteFlights(): Flow<List<FavoriteFlight>> = flightsDao.getFavoriteFlights()

    override fun getSearched(name: String): Flow<Flight> = flightsDao.getSearched(name)

    override fun getOthers(name: String): Flow<List<Flight>> = flightsDao.getOthers(name)

    override suspend fun addFavoriteFlight(favoriteFlight: FavoriteFlight) = flightsDao.insertInFavorite(favoriteFlight)

    override suspend fun deleteFavoriteFlight(favoriteFlight: FavoriteFlight) = flightsDao.deleteFavorite(favoriteFlight)
}
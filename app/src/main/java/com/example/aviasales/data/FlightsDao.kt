package com.example.aviasales.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightsDao {
    @Query("select * from airport where iata_code = :code ORDER BY passengers DESC")
    fun getFromIataCode(code: String): Flow<List<Flight>>

    @Query("SELECT * from airport WHERE name LIKE :name || '%'  OR iata_code LIKE :name || '%' LIMIT 8")
    fun searchAirport(name: String?): Flow<List<Flight>>

    @Query("select * from favorite")
    fun getFavoriteFlights(): Flow<List<FavoriteFlight>>

    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = FavoriteFlight::class)
    suspend fun insertInFavorite(favoriteFlight: FavoriteFlight)

    @Delete(entity = FavoriteFlight::class)
    suspend fun deleteFavorite(favoriteFlight: FavoriteFlight)

    @Query("SELECT * from airport WHERE iata_code = :name ORDER BY passengers DESC")
    fun getSearched(name: String): Flow<Flight>

    @Query("SELECT * from airport WHERE NOT iata_code = :name ORDER BY passengers DESC")
    fun getOthers(name: String): Flow<List<Flight>>

}
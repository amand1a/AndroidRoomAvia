package com.example.aviasales.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteFlight::class, Flight::class], version = 1, exportSchema = false)
abstract class AviasalesDatabase: RoomDatabase() {

    abstract fun FlightDAO(): FlightsDao

    companion object {
        @Volatile
        private var Instance: AviasalesDatabase? = null

        fun getDatabase(context: Context): AviasalesDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AviasalesDatabase::class.java, "flight_search")
                    /**
                     * Setting this option in your app's database builder means that Room
                     * permanently deletes all data from the tables in your database when it
                     * attempts to perform a migration with no defined migration path.
                     */
                    .createFromAsset("database/flight_search.db")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
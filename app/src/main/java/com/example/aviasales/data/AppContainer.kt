package com.example.aviasales.data

import android.content.Context

interface AppContainer{

    val flightsRepository: FlightsRepository

}


class AppDataContainer(private val context: Context): AppContainer{

    override val flightsRepository: FlightsRepository by lazy {
        OfflineFlightRepository(AviasalesDatabase.getDatabase(context).FlightDAO())
    }
}
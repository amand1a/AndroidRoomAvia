package com.example.aviasales

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.aviasales.data.AppContainer
import com.example.aviasales.data.AppDataContainer
import com.example.aviasales.data.UserRepository


private const val SEARCH_QUERY_NAME = "search_query"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = SEARCH_QUERY_NAME
)

class AviasalesApplication: Application() {

    lateinit var container: AppContainer

    lateinit var userRepository: UserRepository
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        userRepository = UserRepository(dataStore)
    }
}
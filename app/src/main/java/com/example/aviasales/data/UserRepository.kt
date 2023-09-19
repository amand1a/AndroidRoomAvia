package com.example.aviasales.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map



class UserRepository(private val dataStore: DataStore<Preferences>){

   val searchQuery: Flow<String> = dataStore.data.map {
       it[SEARCH_LAYOUT] ?: ""
   }

    suspend fun updateQuery(query: String){
         dataStore.edit {
                it[SEARCH_LAYOUT] = query
         }
    }

    private companion object {
        val SEARCH_LAYOUT = stringPreferencesKey("search_query")
    }
}
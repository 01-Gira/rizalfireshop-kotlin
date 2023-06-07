package com.gira.rizalfireshop.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.gira.rizalfireshop.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


//private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")
class UserPreference private constructor(private val dataStore: DataStore<Preferences>){

    fun getToken():Flow<String>{
        return  dataStore.data.map { preferences->
            preferences[TOKEN_KEY] ?: ""
        }
    }

    fun getName():Flow<String>{
        return  dataStore.data.map { preferences->
            preferences[NAME_KEY] ?: ""
        }
    }

    fun getInfoUser():Flow<User>{
        return  dataStore.data.map { preferences ->
            User(
                preferences[NAME_KEY] ?: "",
                preferences[TOKEN_KEY] ?: ""
            )
        }
    }

    fun isLogin():Flow<Boolean>{
        return dataStore.data.map { preferences ->
            preferences[STATE_KEY] ?: false
        }
    }

    suspend fun saveInfoUser(token:String,name:String){
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
            preferences[NAME_KEY] = name
        }
    }

    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = false
            preferences[TOKEN_KEY] = ""
            preferences[NAME_KEY]  = ""
        }
    }
    companion object{
        @Volatile
        private var INSTANCE: UserPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }

        private val TOKEN_KEY = stringPreferencesKey("token")
        private val NAME_KEY = stringPreferencesKey("name")
        private val STATE_KEY = booleanPreferencesKey("state")
    }
}
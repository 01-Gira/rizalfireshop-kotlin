package com.gira.rizalfireshop.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.gira.rizalfireshop.data.local.UserPreference
import com.gira.rizalfireshop.data.local.repository.ProductRepository
import com.gira.rizalfireshop.data.local.repository.UserRepository
import com.gira.rizalfireshop.data.remote.retrofit.ApiConfig

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")
object Injection {
    fun userRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val userPreference = UserPreference.getInstance(context.dataStore)
        return UserRepository(apiService,userPreference)
    }

    fun productRepository(context: Context): ProductRepository {
        val apiService = ApiConfig.getApiService()
        val userPreference = UserPreference.getInstance(context.dataStore)
        return ProductRepository(apiService)
    }
}
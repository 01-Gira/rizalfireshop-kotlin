package com.gira.rizalfireshop.data.remote.retrofit

import com.gira.rizalfireshop.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

object ApiConfig {
    private const val IP_Address: String =BuildConfig.IP_Address

    fun getApiService():ApiService {
        val gsonBuilder = GsonBuilder().apply {
            setLenient()
        }
        val gson = gsonBuilder.create()
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(IP_Address)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }


}
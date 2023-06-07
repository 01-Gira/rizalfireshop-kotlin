package com.gira.rizalfireshop.data.remote.retrofit

import com.gira.rizalfireshop.data.remote.response.LoginResponse
import com.gira.rizalfireshop.data.remote.response.ProductsResponse
import com.gira.rizalfireshop.data.remote.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name : String,
        @Field("email") email : String,
        @Field("password") password : String,
    ) : RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse

    @GET("products")
    suspend fun getProducts(
    ): ProductsResponse
}
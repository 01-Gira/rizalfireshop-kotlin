package com.gira.rizalfireshop.data.local.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.gira.rizalfireshop.data.local.UserPreference
import com.gira.rizalfireshop.data.model.User
import com.gira.rizalfireshop.data.remote.response.LoginResult
import com.gira.rizalfireshop.data.remote.response.RegisterResponse
import com.gira.rizalfireshop.data.remote.retrofit.ApiService
import com.gira.rizalfireshop.utils.Result
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
) {

    fun getResponseLogin(email: String, password: String): LiveData<Result<LoginResult>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.login(email, password)
                if (!response.error) {
                    emit(Result.Success(response.loginResult))
                    response.loginResult.token
                }else{
                    Log.e(TAG, "Login Fail: ${response.message}")
                    emit(Result.Error(response.message))
                }
            }catch (e: Exception){
                Log.e(TAG, "Login Exception: ${e.message.toString()}")
                emit(Result.Error(e.message.toString()))
            }
        }

    fun getResponseRegister(name: String, email: String, password: String) : LiveData<Result<RegisterResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.register(name, email, password)
                if (!response.error) {
                    emit(Result.Success(response))
                } else {
                    Log.e(TAG, "Register Fail: ${response.message}")
                    emit(Result.Error(response.message))

                }
            }catch (e: Exception) {
                Log.e(TAG, "Register Exception: ${e.message.toString()}")
                emit(Result.Error(e.message.toString()))
            }
        }

    fun getToken(): LiveData<String> = userPreference.getToken().asLiveData()
    fun getName(): LiveData<String> = userPreference.getName().asLiveData()

    fun getInfoUser(): Flow<User> = userPreference.getInfoUser()

    suspend fun saveToken(name: String, token: String) {
        userPreference.saveInfoUser(name, token)
    }

    suspend fun loginState() {
        userPreference.login()
    }

    fun isLogin():LiveData<Boolean> =userPreference.isLogin().asLiveData()

    suspend fun logout() {
        userPreference.logout()
    }
}
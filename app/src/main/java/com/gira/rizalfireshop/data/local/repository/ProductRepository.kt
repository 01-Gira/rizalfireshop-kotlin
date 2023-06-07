package com.gira.rizalfireshop.data.local.repository

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.gira.rizalfireshop.data.model.Product
import com.gira.rizalfireshop.data.remote.retrofit.ApiService
import com.gira.rizalfireshop.utils.Result

class ProductRepository(
    private val apiService: ApiService,
) {
    fun getProducts() : LiveData<Result<List<Product>>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getProducts()
                if (!response.error) {
//                    emit(Result.Success(response.listProduct))
                    if (response.data?.isNotEmpty() == true) {
                        emit(Result.Success(response.data))
                    } else {
                        emit(Result.Error("Data is empty"))
                    }
                }else {
                    Log.e(TAG, "Get Products Fail : ${response.message}")
                    emit(Result.Error(response.message))
                }
            }catch (e: Exception) {
                Log.e(TAG, "Get Products Exception : ${e.message.toString()}")
                emit(Result.Error(e.message.toString()))
            }
        }
}
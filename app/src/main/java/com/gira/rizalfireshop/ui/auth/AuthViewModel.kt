package com.gira.rizalfireshop.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gira.rizalfireshop.data.local.repository.UserRepository
import com.gira.rizalfireshop.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: UserRepository) : ViewModel() {
    fun isLogin():LiveData<Boolean> = repository.isLogin()

    fun loginState() {
        viewModelScope.launch {
            repository.loginState()
        }
    }

    fun saveToken(name: String, token: String) {
        viewModelScope.launch {
            repository.saveToken(name, token)
        }
    }

    fun getToken(): LiveData<String> = repository.getToken()

    fun getName(): LiveData<String> = repository.getName()

    fun getInfoUser() : Flow<User> = repository.getInfoUser()

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}
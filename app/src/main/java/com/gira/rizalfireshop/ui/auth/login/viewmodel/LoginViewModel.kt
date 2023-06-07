package com.gira.rizalfireshop.ui.auth.login.viewmodel

import androidx.lifecycle.ViewModel
import com.gira.rizalfireshop.data.local.repository.UserRepository


class LoginViewModel (private val userRepository: UserRepository) : ViewModel() {
    fun getResponseLogin(email: String, password: String) =
        userRepository.getResponseLogin(email, password)

}
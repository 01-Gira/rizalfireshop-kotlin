package com.gira.rizalfireshop.ui.auth.register.viewmodel

import androidx.lifecycle.ViewModel
import com.gira.rizalfireshop.data.local.repository.UserRepository

class RegisterViewModel(private val userRepository: UserRepository): ViewModel() {
    fun getResponseRegister(name: String, email: String, password: String) =
        userRepository.getResponseRegister(name, email, password)
}
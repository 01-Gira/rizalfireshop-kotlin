package com.gira.rizalfireshop.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gira.rizalfireshop.data.di.Injection
import com.gira.rizalfireshop.data.local.UserPreference
import com.gira.rizalfireshop.ui.auth.AuthViewModel
import com.gira.rizalfireshop.ui.auth.login.viewmodel.LoginViewModel
import com.gira.rizalfireshop.ui.auth.register.viewmodel.RegisterViewModel
import com.gira.rizalfireshop.ui.home.viewmodel.ProductViewModel

@Suppress("UNCHECKED_CAST", "UNREACHABLE_CODE")
class ViewModelFactory(
    private val pref: UserPreference,
    private val context: Context
    ) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                return LoginViewModel(Injection.userRepository(context)) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                return RegisterViewModel(Injection.userRepository(context)) as T
            }
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                return AuthViewModel(Injection.userRepository(context)) as T
            }

            modelClass.isAssignableFrom(ProductViewModel::class.java) -> {
                return ProductViewModel(Injection.productRepository(context)) as T
            }

//            modelClass.isAssignableFrom()
            else -> throw IllegalArgumentException("Unknown RegisterViewModel class: "+modelClass.name)
        }

    }

}
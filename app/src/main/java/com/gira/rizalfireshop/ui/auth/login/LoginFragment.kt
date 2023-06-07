package com.gira.rizalfireshop.ui.auth.login

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.gira.rizalfireshop.MainActivity
import com.gira.rizalfireshop.R
import com.gira.rizalfireshop.data.local.UserPreference
import com.gira.rizalfireshop.databinding.FragmentLoginBinding
import com.gira.rizalfireshop.ui.ViewModelFactory
import com.gira.rizalfireshop.ui.auth.AuthViewModel
import com.gira.rizalfireshop.ui.auth.login.viewmodel.LoginViewModel
import java.util.regex.Matcher
import java.util.regex.Pattern
import com.gira.rizalfireshop.utils.Result

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class LoginFragment : Fragment() {

    private val loginViewModel: LoginViewModel by viewModels {
        requireContext()?.dataStore?.let { UserPreference.getInstance(it) }
            ?.let { ViewModelFactory(it,requireContext()) }!!
    }

    private val authViewModel: AuthViewModel by viewModels {
        requireContext()?.dataStore?.let { UserPreference.getInstance(it) }
            ?.let { ViewModelFactory(it,requireContext()) }!!
    }

    private var _binding : FragmentLoginBinding ?= null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        authViewModel.isLogin().observe(this) {
            if(it) {
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnRegister.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        actionLogin()
    }

    private fun actionLogin() {
        binding.apply {
            btnLogin.setOnClickListener {
                val email = edEmailLogin.text.toString()
                val password = edPasswordLogin.text.toString()
                when{
                    email.isEmpty() -> {
                        edEmailLogin.error = getString(R.string.enter_email)
                    }
                    password.isEmpty() -> {
                        edPasswordLogin.error = getString(R.string.enter_password)
                    }
                    password.length < 6 -> {
                        edPasswordLogin.error = getString(R.string.password_length)
                    }
                    !isEmailValid(email) -> {
                        edEmailLogin.error = getString(R.string.invalid_email)
                    }
                    else -> {
                        loginViewModel.getResponseLogin(email, password).observe(viewLifecycleOwner) {
                            when(it) {
                                is Result.Success -> {
                                    showLoading(false)
//                                    btnLogin.visibility = View.VISIBLE
                                    AlertDialog.Builder(context).apply {
                                        setTitle(getString(R.string.success))
                                        setMessage(getString(R.string.sign_in_successfully))
                                        setPositiveButton(getString(R.string.cont)) { _, _ ->
//                                            findNavController().navigate(R.id.action_loginFragment_to_app_navigation)
                                            show().dismiss()
                                            val intent = Intent(context, MainActivity::class.java)
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                            startActivity(intent)
                                        }
                                        create()
                                        show()
                                    }
                                    authViewModel.loginState()
                                    authViewModel.saveToken(
                                        it.data.name,
                                        it.data.token
                                    )
//                                    authViewModel.loginState()
                                }
                                is Result.Loading -> {
                                    showLoading(true)
//                                    btnLogin.visibility = View.GONE
                                }
                                is Result.Error -> {
                                    showLoading(false)
//                                    btnLogin.visibility = View.VISIBLE

                                    AlertDialog.Builder(context).apply {
                                        setTitle(getString(R.string.fail))
                                        setMessage(it.error)
                                        setPositiveButton(getString(R.string.cont)) {_, _ ->
                                            show().dismiss()
                                        }
                                        create()
                                        show()

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            pbLogin.visibility = if (isLoading) View.VISIBLE else View.GONE
            btnLogin.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
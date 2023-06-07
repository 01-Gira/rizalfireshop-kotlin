package com.gira.rizalfireshop.ui.auth.register

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.gira.rizalfireshop.R
import com.gira.rizalfireshop.data.local.UserPreference
import com.gira.rizalfireshop.databinding.FragmentRegisterBinding
import com.gira.rizalfireshop.ui.ViewModelFactory
import com.gira.rizalfireshop.ui.auth.AuthViewModel
import com.gira.rizalfireshop.ui.auth.login.viewmodel.LoginViewModel
import com.gira.rizalfireshop.ui.auth.register.viewmodel.RegisterViewModel
import com.gira.rizalfireshop.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Matcher
import java.util.regex.Pattern

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class RegisterFragment : Fragment() {

    private var _binding : FragmentRegisterBinding ?= null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels {
        requireContext()?.dataStore?.let { UserPreference.getInstance(it) }
            ?.let { ViewModelFactory(it,requireContext()) }!!
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = requireContext().dataStore


        registerAction()
        binding.btnBack.setOnClickListener {
            it.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun registerAction() {
        binding.apply {
            btnRegister.setOnClickListener {
                val name = edNameRegister.text.toString()
                val email = edEmailRegister.text.toString()
                val pass = edPasswordRegister.text.toString()
                val cpass = edCPasswordRegister.text.toString()

                when {
                    name.isEmpty() -> {
                        edNameRegister.error = getString(R.string.enter_name)
                    }
                    email.isEmpty() -> {
                        edEmailRegister.error = getString(R.string.enter_email)
                    }
                    !isEmailValid(email) -> {
                        edEmailRegister.error = getString(R.string.invalid_email)
                    }
                    pass.isEmpty() -> {
                        edPasswordRegister.error = getString(R.string.enter_password)
                    }
                    cpass.isEmpty() -> {
                        edCPasswordRegister.error = getString(R.string.enter_confirm_password)
                    }
                    pass.length < 6 && cpass.length < 6 -> {
                        edPasswordRegister.error = getString(R.string.password_length)
                        edCPasswordRegister.error = getString(R.string.password_length)
                    }
                    (pass != cpass) -> {
                        edPasswordRegister.error = getString(R.string.password_not_match)
                        edCPasswordRegister.error = getString(R.string.password_not_match)
                    }
                    else -> {
                        viewModel.getResponseRegister(name, email, cpass).observe(viewLifecycleOwner) {
                            when(it) {
                                is Result.Success -> {
//                                    btnRegister.visibility = View.VISIBLE

                                    showLoading(false)
                                    AlertDialog.Builder(context).apply {
                                        setTitle(getString(R.string.success))
                                        setMessage(getString(R.string.success_register_account))
                                        setPositiveButton(getString(R.string.cont)) {_, _ ->
                                            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                                            show().dismiss()
//                                            val intent = Intent(context, MainActivity::class.java)
//                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//                                            startActivity(intent)
                                        }
                                        create()
                                        show()

                                    }
                                }
                                is Result.Loading -> {
                                    showLoading(true)
//                                    btnRegister.visibility = View.GONE

                                }
                                is Result.Error -> {
                                    showLoading(false)
//                                    btnRegister.visibility = View.VISIBLE
                                    AlertDialog.Builder(context).apply {
                                        setTitle(getString(R.string.fail))
                                        setMessage(it.error)
                                        setPositiveButton(getString(R.string.cont)) {_, _ ->
                                            show().dismiss()
                                        }
                                        create()
                                        show()

                                    }
                                    if(it.error == "The email has already been taken.") {
                                        edEmailRegister.error = it.error
                                        edEmailRegister.requestFocus()
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
            pbRegister.visibility = if (isLoading) View.VISIBLE else View.GONE
            btnRegister.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
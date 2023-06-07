package com.gira.rizalfireshop.ui.profile

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import com.gira.rizalfireshop.R
import com.gira.rizalfireshop.data.local.UserPreference
import com.gira.rizalfireshop.data.model.User
import com.gira.rizalfireshop.databinding.FragmentProfileBinding
import com.gira.rizalfireshop.ui.ViewModelFactory
import com.gira.rizalfireshop.ui.auth.AuthActivity
import com.gira.rizalfireshop.ui.auth.AuthViewModel
import kotlinx.coroutines.flow.collect

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")
class ProfileFragment : Fragment() {
    private var _binding : FragmentProfileBinding ?= null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels {
        requireContext().dataStore.let { UserPreference.getInstance(it) }
            .let { ViewModelFactory(it,requireContext()) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cekLoginState()

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(context).apply {
                setTitle(getString(R.string.fail))
                setMessage(getString(R.string.are_you_sure))
                setPositiveButton(getString(R.string.logout)) {_, _ ->
                    cekLoginState()
                    authViewModel.logout()
                }
                setNegativeButton(getString(R.string.cancel)) {_, _ ->
                    show().dismiss()
                }
                create()
                show()

            }
        }
    }

    private fun cekLoginState() {
        authViewModel.isLogin().observe(viewLifecycleOwner) {
            if(!it) {
                val intent = Intent(context, AuthActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        authViewModel.getName().observe(viewLifecycleOwner) {
            binding.tvUserName.text = it
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbProfile.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
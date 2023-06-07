package com.gira.rizalfireshop.ui.home

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gira.rizalfireshop.R
import com.gira.rizalfireshop.adapter.LatestProductAdapter
import com.gira.rizalfireshop.data.local.UserPreference
import com.gira.rizalfireshop.databinding.FragmentHomeBinding
import com.gira.rizalfireshop.ui.ViewModelFactory
import com.gira.rizalfireshop.ui.auth.AuthActivity
import com.gira.rizalfireshop.ui.auth.AuthViewModel
import com.gira.rizalfireshop.ui.home.viewmodel.ProductViewModel
import com.gira.rizalfireshop.utils.Result

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")
class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding ?= null
    private val binding get() = _binding!!


    private val authViewModel: AuthViewModel by viewModels {
        requireContext().dataStore.let { UserPreference.getInstance(it) }
            .let { ViewModelFactory(it,requireContext()) }
    }

    private val productViewModel: ProductViewModel by viewModels {
        requireContext().dataStore.let { UserPreference.getInstance(it) }
            .let { ViewModelFactory(it, requireContext()) }
    }

    private lateinit var token: String

    private val latestProductAdapter by lazy { LatestProductAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cekLoginState()
        setupProductsAdapter()
    }

    private fun setupProductsAdapter() {
        binding.rvListProductsLatest.apply {
            layoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
            adapter = latestProductAdapter

            fun updateDataEmptyVisibility() {
                if (latestProductAdapter.isDataEmpty()) {
                    binding.tvDataEmpty.visibility = View.VISIBLE
                } else {
                    binding.tvDataEmpty.visibility = View.GONE
                }
            }

            // Observasi perubahan data pada adapter
            latestProductAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onChanged() {
                    updateDataEmptyVisibility()
                }

                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    updateDataEmptyVisibility()
                }

                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                    updateDataEmptyVisibility()
                }
            })

            productViewModel.getProducts().observe(viewLifecycleOwner) {
                when (it) {
                    is Result.Success -> {
                        showLoading(false)
                        latestProductAdapter.differ.submitList(it.data)
                    }
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Error -> {
                        showLoading(false)
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

    private fun cekLoginState() {
        authViewModel.isLogin().observe(viewLifecycleOwner) {
            if(!it) {
                val intent = Intent(context, AuthActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        authViewModel.getToken().observe(viewLifecycleOwner) {
            token = it
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbHome.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
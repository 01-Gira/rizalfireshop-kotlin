package com.gira.rizalfireshop.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import com.gira.rizalfireshop.data.local.repository.ProductRepository

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel() {
    fun getProducts() = productRepository.getProducts()
}
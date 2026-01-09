package fr.delplanque.tp_androidstudio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories

    init {
        loadCategories()
        loadProducts()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                _categories.value = listOf("all") + RetrofitInstance.api.getCategories()
            } catch (e: Exception) {}
        }
    }

    fun loadProducts(category: String = "all") {
        viewModelScope.launch {
            try {
                _products.value = if (category == "all") {
                    RetrofitInstance.api.getProducts()
                } else {
                    RetrofitInstance.api.getProductsByCategory(category)
                }
            } catch (e: Exception) {}
        }
    }
}
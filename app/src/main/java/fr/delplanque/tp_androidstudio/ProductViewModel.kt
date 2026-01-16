package fr.delplanque.tp_androidstudio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted


class ProductViewModel : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories

    private val _sortOption = MutableStateFlow(SortOption.NONE)
    val sortOption: StateFlow<SortOption> = _sortOption

    val sortedProducts: StateFlow<List<Product>> = combine(_products, _sortOption) { products, sort ->
        when (sort) {
            SortOption.PRICE_ASC -> products.sortedBy { it.price }
            SortOption.PRICE_DESC -> products.sortedByDescending { it.price }
            SortOption.RATING_DESC -> products.sortedByDescending { it.rating.rate }
            SortOption.NONE -> products
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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

    fun setSortOption(option: SortOption) {
        _sortOption.value = option
    }
}
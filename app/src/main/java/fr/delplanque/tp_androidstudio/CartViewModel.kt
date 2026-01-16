package fr.delplanque.tp_androidstudio

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.map


class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).cartDao()

    // Liste des produits dans le panier (observée par l'UI)
    val cartItems: StateFlow<List<CartEntity>> = dao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Calcul du total en temps réel [cite: 162]
    val totalPrice: StateFlow<Double> = cartItems.map { items ->
        items.sumOf { it.price * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val cartCount: StateFlow<Int> = cartItems.map { items ->
        items.sumOf { it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Ajouter au panier (gère l'incrémentation si existe déjà) [cite: 159]
    fun addToCart(product: Product) {
        viewModelScope.launch {
            val existingItem = dao.getProductById(product.id)
            if (existingItem != null) {
                // Si le produit existe, on augmente la quantité
                val updatedItem = existingItem.copy(quantity = existingItem.quantity + 1)
                dao.insert(updatedItem)
            } else {
                // Sinon on l'ajoute
                val newItem = CartEntity(
                    id = product.id,
                    title = product.title,
                    price = product.price,
                    image = product.image,
                    quantity = 1
                )
                dao.insert(newItem)
            }
        }
    }

    // Supprimer ou décrémenter un produit [cite: 158]
    fun removeFromCart(item: CartEntity) {
        viewModelScope.launch {
            if (item.quantity > 1) {
                dao.insert(item.copy(quantity = item.quantity - 1))
            } else {
                dao.delete(item)
            }
        }
    }

    fun increaseQuantity(item: CartEntity) {
        viewModelScope.launch {
            dao.insert(item.copy(quantity = item.quantity + 1))
        }
    }

    // Supprimer complètement une ligne (optionnel mais pratique)
    fun deleteItem(item: CartEntity) {
        viewModelScope.launch {
            dao.delete(item)
        }
    }
}
package fr.delplanque.tp_androidstudio

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).cartDao()

    // Panier
    val cartItems: StateFlow<List<CartEntity>> = dao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Historique (Nouveau)
    val orderHistory: StateFlow<List<OrderEntity>> = dao.getAllOrders()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Total
    val totalPrice: StateFlow<Double> = cartItems.map { items ->
        items.sumOf { it.price * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // --- FONCTION MANQUANTE CORRIGÉE ---
    fun validateOrder() {
        viewModelScope.launch {
            val currentItems = cartItems.value
            if (currentItems.isNotEmpty()) {
                val total = currentItems.sumOf { it.price * it.quantity }
                val count = currentItems.sumOf { it.quantity }

                // 1. Créer la commande
                val order = OrderEntity(
                    date = System.currentTimeMillis(),
                    totalAmount = total,
                    itemCount = count
                )
                // 2. Sauvegarder dans l'historique
                dao.insertOrder(order)
                // 3. Vider le panier
                dao.clearCart()
            }
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            val existingItem = dao.getProductById(product.id)
            if (existingItem != null) {
                val updatedItem = existingItem.copy(quantity = existingItem.quantity + 1)
                dao.insert(updatedItem)
            } else {
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

    fun deleteItem(item: CartEntity) {
        viewModelScope.launch {
            dao.delete(item)
        }
    }
}
package fr.delplanque.tp_androidstudio

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Index
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).cartDao()

    val cartItems: StateFlow<List<CartEntity>> = dao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val totalPrice: StateFlow<Double> = cartItems.map { items ->
        items.sumOf { it.price * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
    val orderHistory: Flow<List<OrderEntity>> = dao.getAllOrders()
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
    fun valideOrder() {
        viewModelScope.launch {
            val currentCart = _cartItems.value

            if (currentCart.isNotEmpty()) {
                val totalAmount = currentCart.sumOf { it.price * it.quantity }
                val itemCount = currentCart.sumOf { it.quantity }

                // 2. Créer la commande
                val newOrder = OrderEntity(
                    date = System.currentTimeMillis(),
                    totalAmount = totalAmount,
                    itemCount = itemCount
                )

                // 3. Sauvegarder l'historique
                dao.insertOrder(newOrder)

                // 4. Vider le panier
                dao.clearCart()
            }
        }
    }
}
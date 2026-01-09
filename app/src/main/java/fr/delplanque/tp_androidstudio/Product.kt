package fr.delplanque.tp_androidstudio
import com.google.gson.annotations.SerializedName

data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating
)

data class Rating(
    val rate: Double,
    val count: Int
)

// Classe pour gérer le panier avec quantité [cite: 22, 24]
data class CartItem(
    val product: Product,
    var quantity: Int
)
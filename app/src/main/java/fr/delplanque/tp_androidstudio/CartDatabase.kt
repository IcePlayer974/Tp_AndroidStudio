package fr.delplanque.tp_androidstudio

import androidx.room.*

@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey val id: Int,
    val product: Product,
    val quantity: Int
)
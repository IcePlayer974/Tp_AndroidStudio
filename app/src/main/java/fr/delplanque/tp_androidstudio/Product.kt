package fr.delplanque.tp_androidstudio

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    @PrimaryKey val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val imageUrl: String,
    val rating: Rating
)

data class Rating(
    val rate: Double,
    val count: Int
)
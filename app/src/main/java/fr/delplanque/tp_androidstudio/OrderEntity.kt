package fr.delplanque.tp_androidstudio

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,
    val totalAmount: Double,
    val itemCount: Int
)
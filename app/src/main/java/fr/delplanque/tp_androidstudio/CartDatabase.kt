package fr.delplanque.tp_androidstudio

import androidx.room.*

@Entity(tableName = "cart_items")data class CartEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val price: Double,
    val image: String,
    val quantity: Int
)

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    suspend fun getAll(): List<CartEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartEntity)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun delete(id: Int)
}

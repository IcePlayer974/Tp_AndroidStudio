package fr.delplanque.tp_androidstudio

import android.content.Context
import androidx.room.*

@Entity(tableName = "cart_items")
data class CartEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val price: Double,
    val image: String,
    val quantity: Int
)

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun getAll(): kotlinx.coroutines.flow.Flow<List<CartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartEntity)

    @Delete
    suspend fun delete(item: CartEntity)

    @Query("SELECT * FROM cart_items WHERE id = :id LIMIT 1")
    suspend fun getProductById(id: Int): CartEntity?

    @Query("SELECT * FROM orders ORDER BY date DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Insert
    suspend fun insertOrder(order: OrderEntity)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}


@Database(entities = [CartEntity::class, OrderEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ecommerce_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
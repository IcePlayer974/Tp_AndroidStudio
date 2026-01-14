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
    // Utiliser Flow pour que l'UI se mette à jour automatiquement
    fun getAll(): kotlinx.coroutines.flow.Flow<List<CartEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartEntity)

    @Delete
    suspend fun delete(item: CartEntity)

    // Pour vérifier si un produit existe déjà
    @Query("SELECT * FROM cart_items WHERE id = :id LIMIT 1")
    suspend fun getProductById(id: Int): CartEntity?
}

// Ajout de la configuration de la base de données
@Database(entities = [CartEntity::class], version = 1)
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
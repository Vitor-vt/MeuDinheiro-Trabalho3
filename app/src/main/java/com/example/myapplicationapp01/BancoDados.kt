package com.example.myapplicationprimer

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "movimentacoes")
data class MovimentacaoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val descricao: String,
    val valor: Double,
    val tipo: String,
    val data: Long = System.currentTimeMillis()
)

@Entity(tableName = "movimentacoes_caixinha")
data class MovimentacaoCaixinhaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val valor: Double,
    val tipo: String,
    val data: Long = System.currentTimeMillis()
)

@Dao
interface MovimentacaoDao {
    @Query("SELECT * FROM movimentacoes ORDER BY id DESC")
    fun observarTodas(): Flow<List<MovimentacaoEntity>>
    @Query("SELECT * FROM movimentacoes WHERE id = :id LIMIT 1")
    fun observarPorId(id: Long): Flow<MovimentacaoEntity?>
    @Insert suspend fun inserir(item: MovimentacaoEntity): Long
    @Update suspend fun atualizar(item: MovimentacaoEntity)
    @Delete suspend fun excluir(item: MovimentacaoEntity)
}

@Dao
interface CaixinhaDao {
    @Query("SELECT * FROM movimentacoes_caixinha ORDER BY id DESC")
    fun observarTodas(): Flow<List<MovimentacaoCaixinhaEntity>>
    @Query("SELECT * FROM movimentacoes_caixinha WHERE id = :id LIMIT 1")
    fun observarPorId(id: Long): Flow<MovimentacaoCaixinhaEntity?>
    @Insert suspend fun inserir(item: MovimentacaoCaixinhaEntity): Long
    @Update suspend fun atualizar(item: MovimentacaoCaixinhaEntity)
    @Delete suspend fun excluir(item: MovimentacaoCaixinhaEntity)
}

@Database(entities = [MovimentacaoEntity::class, MovimentacaoCaixinhaEntity::class], version = 1, exportSchema = false)
abstract class MeuDinheiroDatabase : RoomDatabase() {
    abstract fun movimentacaoDao(): MovimentacaoDao
    abstract fun caixinhaDao(): CaixinhaDao

    companion object {
        @Volatile private var instancia: MeuDinheiroDatabase? = null
        fun obter(context: Context): MeuDinheiroDatabase = instancia ?: synchronized(this) {
            instancia ?: Room.databaseBuilder(context.applicationContext, MeuDinheiroDatabase::class.java, "meu_dinheiro.db")
                .build().also { instancia = it }
        }
    }
}

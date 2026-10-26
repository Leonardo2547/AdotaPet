package com.example.adotapet.data.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "interesses_adocao")
data class InteresseAdocao(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val animalId: Long,
    val nomeInteressado: String,
    val telefone: String,
    val moradia: String,
    val experiencia: String,
    val criadoEm: Long = System.currentTimeMillis()
)

@Dao
interface InteresseAdocaoDao {
    @Insert suspend fun inserir(interesse: InteresseAdocao)
    @Query("SELECT * FROM interesses_adocao ORDER BY criadoEm DESC")
    fun listarTodos(): Flow<List<InteresseAdocao>>
}

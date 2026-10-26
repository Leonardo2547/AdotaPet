package com.example.adotapet.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimalDao {

    @Query("SELECT * FROM animais ORDER BY criadoEm DESC")
    fun listarTodos(): Flow<List<AnimalEntity>>

    @Insert
    suspend fun inserir(animal: AnimalEntity)

    @Insert
    suspend fun inserirTodos(animais: List<AnimalEntity>)

    @Update
    suspend fun atualizar(animal: AnimalEntity)

    @Delete
    suspend fun excluir(animal: AnimalEntity)

    @Query("DELETE FROM animais WHERE finalizado = 1")
    suspend fun excluirFinalizados()
}

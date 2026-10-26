package com.example.adotapet.data.repository

import com.example.adotapet.data.local.AnimalDao
import com.example.adotapet.data.local.AnimalEntity
import com.example.adotapet.data.local.InteresseAdocao
import com.example.adotapet.data.local.InteresseAdocaoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AnimalRepository(
    private val animalDao: AnimalDao,
    private val interesseDao: InteresseAdocaoDao
) {

    suspend fun registrarInteresse(interesse: InteresseAdocao) {
        withContext(Dispatchers.IO) { interesseDao.inserir(interesse) }
    }

    fun listarInteresses(): Flow<List<InteresseAdocao>> = interesseDao.listarTodos()

    fun listarTodos(): Flow<List<AnimalEntity>> {
        return animalDao.listarTodos()
    }

    suspend fun inserir(animal: AnimalEntity) {
        withContext(Dispatchers.IO) {
            animalDao.inserir(animal)
        }
    }

    suspend fun inserirTodos(animais: List<AnimalEntity>) {
        withContext(Dispatchers.IO) {
            animalDao.inserirTodos(animais)
        }
    }

    suspend fun atualizar(animal: AnimalEntity) {
        withContext(Dispatchers.IO) {
            animalDao.atualizar(animal)
        }
    }

    suspend fun excluir(animal: AnimalEntity) {
        withContext(Dispatchers.IO) {
            animalDao.excluir(animal)
        }
    }

    suspend fun excluirFinalizados() {
        withContext(Dispatchers.IO) {
            animalDao.excluirFinalizados()
        }
    }
}

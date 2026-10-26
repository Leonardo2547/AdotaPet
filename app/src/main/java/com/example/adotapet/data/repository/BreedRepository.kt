package com.example.adotapet.data.repository

import com.example.adotapet.data.remote.DogApi
import com.example.adotapet.model.DogFoto
import com.example.adotapet.model.nomeDaRaca
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BreedRepository(
    private val api: DogApi
) {

    suspend fun buscarFotos(quantidade: Int): List<DogFoto> {
        return withContext(Dispatchers.IO) {
            api.imagensAleatorias(quantidade).message.map { url ->
                DogFoto(url = url, raca = url.nomeDaRaca())
            }
        }
    }
}

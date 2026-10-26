package com.example.adotapet.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.adotapet.data.local.AnimalDatabase
import com.example.adotapet.data.preferences.PreferencesRepository
import com.example.adotapet.data.preferences.adotaPetDataStore
import com.example.adotapet.data.remote.RetrofitClient
import com.example.adotapet.data.repository.AnimalRepository
import com.example.adotapet.data.repository.BreedRepository

class AppViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(AnimalViewModel::class.java)) {
            val database = AnimalDatabase.getDatabase(context)
            val repository = AnimalRepository(database.animalDao(), database.interesseAdocaoDao())
            val preferencias = PreferencesRepository(context.adotaPetDataStore)
            return AnimalViewModel(repository, preferencias) as T
        }

        if (modelClass.isAssignableFrom(RacasViewModel::class.java)) {
            val repository = BreedRepository(RetrofitClient.dogApi)
            return RacasViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
    }
}

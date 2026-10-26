package com.example.adotapet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adotapet.data.repository.BreedRepository
import com.example.adotapet.model.DogFoto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface RacasUiState {
    data object Carregando : RacasUiState
    data class Sucesso(val fotos: List<DogFoto>) : RacasUiState
    data class Erro(val mensagem: String) : RacasUiState
}

class RacasViewModel(
    private val repository: BreedRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RacasUiState>(RacasUiState.Carregando)
    val uiState: StateFlow<RacasUiState> = _uiState.asStateFlow()

    init {
        carregar()
    }

    fun carregar() {
        viewModelScope.launch {
            _uiState.value = RacasUiState.Carregando
            try {
                val fotos = repository.buscarFotos(20)
                _uiState.value = RacasUiState.Sucesso(fotos)
            } catch (e: IOException) {
                _uiState.value = RacasUiState.Erro("Sem conexão com a internet. Confira sua rede e tente de novo.")
            } catch (e: Exception) {
                _uiState.value = RacasUiState.Erro("Algo deu errado ao carregar as fotos. Tente de novo.")
            }
        }
    }
}

package com.example.adotapet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adotapet.data.local.AnimalEntity
import com.example.adotapet.data.local.InteresseAdocao
import com.example.adotapet.data.local.DadosIniciais
import com.example.adotapet.data.preferences.PreferencesRepository
import com.example.adotapet.data.repository.AnimalRepository
import com.example.adotapet.model.FiltroAnimal
import com.example.adotapet.model.Metricas
import com.example.adotapet.model.filtrar
import com.example.adotapet.model.metricas
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed interface AnimalUiState {

    data object Carregando : AnimalUiState

    data class Sucesso(
        val todos: List<AnimalEntity>,
        val animais: List<AnimalEntity>,
        val historico: List<AnimalEntity>,
        val metricas: Metricas,
        val filtro: FiltroAnimal,
        val filtrando: Boolean
    ) : AnimalUiState
}

class AnimalViewModel(
    private val repository: AnimalRepository,
    private val preferencias: PreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AnimalUiState>(AnimalUiState.Carregando)
    val uiState: StateFlow<AnimalUiState> = _uiState.asStateFlow()

    private val _animalParaExcluir = MutableStateFlow<AnimalEntity?>(null)
    val animalParaExcluir: StateFlow<AnimalEntity?> = _animalParaExcluir.asStateFlow()

    private val _animalParaFinalizar = MutableStateFlow<AnimalEntity?>(null)
    val animalParaFinalizar: StateFlow<AnimalEntity?> = _animalParaFinalizar.asStateFlow()

    private val _confirmandoLimpeza = MutableStateFlow(false)
    val confirmandoLimpeza: StateFlow<Boolean> = _confirmandoLimpeza.asStateFlow()

    private val _confirmandoDescarte = MutableStateFlow(false)
    val confirmandoDescarte: StateFlow<Boolean> = _confirmandoDescarte.asStateFlow()

    private val _temaEscuro = MutableStateFlow(false)
    val temaEscuro: StateFlow<Boolean> = _temaEscuro.asStateFlow()

    private val _onboardingConcluido = MutableStateFlow<Boolean?>(null)
    val onboardingConcluido: StateFlow<Boolean?> = _onboardingConcluido.asStateFlow()

    private var todos: List<AnimalEntity> = emptyList()
    private var filtro: FiltroAnimal = FiltroAnimal.TODOS
    private var busca: String = ""
    private var listaCarregada = false

    init {
        carregarAnimais()
        carregarPreferencias()
        inserirDadosIniciais()
    }

    private fun carregarAnimais() {
        viewModelScope.launch {
            repository.listarTodos().collect { lista ->
                todos = lista
                listaCarregada = true
                atualizarEstado()
            }
        }
    }

    private fun carregarPreferencias() {
        viewModelScope.launch {
            preferencias.filtro.collect { valor ->
                filtro = valor
                atualizarEstado()
            }
        }
        viewModelScope.launch {
            preferencias.temaEscuro.collect { valor ->
                _temaEscuro.value = valor
            }
        }
        viewModelScope.launch {
            preferencias.onboardingConcluido.collect { valor ->
                _onboardingConcluido.value = valor
            }
        }
    }

    private fun inserirDadosIniciais() {
        viewModelScope.launch {
            if (!preferencias.dadosIniciaisCarregados.first()) {
                repository.inserirTodos(DadosIniciais.animais)
                preferencias.marcarDadosIniciaisCarregados()
            }
        }
    }

    private fun atualizarEstado() {
        if (!listaCarregada) return

        val ativos = todos.filter { !it.finalizado }

        _uiState.value = AnimalUiState.Sucesso(
            todos = todos,
            animais = ativos.filtrar(filtro, busca),
            historico = todos.filter { it.finalizado },
            metricas = todos.metricas(),
            filtro = filtro,
            filtrando = filtro != FiltroAnimal.TODOS || busca.isNotBlank()
        )
    }

    fun alterarBusca(texto: String) {
        busca = texto
        atualizarEstado()
    }

    fun alterarFiltro(novoFiltro: FiltroAnimal) {
        viewModelScope.launch {
            preferencias.definirFiltro(novoFiltro)
        }
    }

    fun cadastrarAnimal(animal: AnimalEntity) {
        viewModelScope.launch {
            repository.inserir(animal)
        }
    }

    fun atualizarAnimal(animal: AnimalEntity) {
        viewModelScope.launch {
            repository.atualizar(animal)
        }
    }

    fun solicitarExclusao(animal: AnimalEntity) {
        _animalParaExcluir.value = animal
    }

    fun cancelarExclusao() {
        _animalParaExcluir.value = null
    }

    fun confirmarExclusao() {
        val animal = _animalParaExcluir.value ?: return
        _animalParaExcluir.value = null
        viewModelScope.launch {
            repository.excluir(animal)
        }
    }

    fun solicitarFinalizacao(animal: AnimalEntity) {
        _animalParaFinalizar.value = animal
    }

    fun cancelarFinalizacao() {
        _animalParaFinalizar.value = null
    }

    fun confirmarFinalizacao() {
        val animal = _animalParaFinalizar.value ?: return
        _animalParaFinalizar.value = null
        viewModelScope.launch {
            repository.atualizar(animal.copy(finalizado = true, emProcessoAdocao = false))
        }
    }

    fun iniciarProcessoAdocao(animal: AnimalEntity) {
        viewModelScope.launch { repository.atualizar(animal.copy(emProcessoAdocao = true, finalizado = false)) }
    }

    fun registrarInteresse(interesse: InteresseAdocao) {
        viewModelScope.launch { repository.registrarInteresse(interesse) }
    }

    fun reabrir(animal: AnimalEntity) {
        viewModelScope.launch {
            repository.atualizar(animal.copy(finalizado = false, emProcessoAdocao = false))
        }
    }

    fun solicitarLimpeza() {
        _confirmandoLimpeza.value = true
    }

    fun cancelarLimpeza() {
        _confirmandoLimpeza.value = false
    }

    fun confirmarLimpeza() {
        _confirmandoLimpeza.value = false
        viewModelScope.launch {
            repository.excluirFinalizados()
        }
    }

    fun solicitarDescarte() {
        _confirmandoDescarte.value = true
    }

    fun fecharDescarte() {
        _confirmandoDescarte.value = false
    }

    fun alterarTema(escuro: Boolean) {
        viewModelScope.launch {
            preferencias.definirTemaEscuro(escuro)
        }
    }

    fun concluirOnboarding() {
        viewModelScope.launch {
            preferencias.concluirOnboarding()
        }
    }
}

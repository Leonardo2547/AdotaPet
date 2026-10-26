package com.example.adotapet.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.adotapet.model.FiltroAnimal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.adotaPetDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "adotapet_preferencias"
)

class PreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {

    private val temaEscuroKey = booleanPreferencesKey("tema_escuro")
    private val onboardingKey = booleanPreferencesKey("onboarding_concluido")
    private val dadosIniciaisKey = booleanPreferencesKey("dados_iniciais_carregados")
    private val filtroKey = stringPreferencesKey("ultimo_filtro")

    val temaEscuro: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[temaEscuroKey] ?: false
    }

    val onboardingConcluido: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[onboardingKey] ?: false
    }

    val dadosIniciaisCarregados: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[dadosIniciaisKey] ?: false
    }

    val filtro: Flow<FiltroAnimal> = dataStore.data.map { prefs ->
        FiltroAnimal.entries.firstOrNull { it.name == prefs[filtroKey] } ?: FiltroAnimal.TODOS
    }

    suspend fun definirTemaEscuro(valor: Boolean) {
        dataStore.edit { it[temaEscuroKey] = valor }
    }

    suspend fun concluirOnboarding() {
        dataStore.edit { it[onboardingKey] = true }
    }

    suspend fun marcarDadosIniciaisCarregados() {
        dataStore.edit { it[dadosIniciaisKey] = true }
    }

    suspend fun definirFiltro(filtro: FiltroAnimal) {
        dataStore.edit { it[filtroKey] = filtro.name }
    }
}

package com.example.adotapet.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.adotapet.data.local.AnimalEntity
import com.example.adotapet.model.FiltroAnimal
import com.example.adotapet.ui.components.AnimalCard
import com.example.adotapet.ui.components.EstadoCarregando
import com.example.adotapet.ui.components.EstadoVazio
import com.example.adotapet.viewmodel.AnimalUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimaisScreen(
    estado: AnimalUiState,
    onNovo: () -> Unit,
    onAbrirDetalhes: (Long) -> Unit,
    onFiltroChange: (FiltroAnimal) -> Unit,
    onBuscaChange: (String) -> Unit,
    onExcluir: (AnimalEntity) -> Unit
) {

    var busca by rememberSaveable { mutableStateOf("") }
    var porteSelecionado by rememberSaveable { mutableStateOf("Todos") }
    var idadeSelecionada by rememberSaveable { mutableStateOf("Todas") }
    var vacinadoSelecionado by rememberSaveable { mutableStateOf<Boolean?>(null) }
    var castradoSelecionado by rememberSaveable { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(Unit) {
        onBuscaChange(busca)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AdotaPet") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNovo) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Cadastrar animal"
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            OutlinedTextField(
                value = busca,
                onValueChange = { novoTexto ->
                    busca = novoTexto
                    onBuscaChange(novoTexto)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Buscar por nome, espécie ou local") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    if (busca.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                busca = ""
                                onBuscaChange("")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Limpar busca"
                            )
                        }
                    }
                },
                singleLine = true
            )

            val filtroAtual = if (estado is AnimalUiState.Sucesso) {
                estado.filtro
            } else {
                FiltroAnimal.TODOS
            }

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FiltroAnimal.entries.forEach { filtro ->
                    FilterChip(
                        selected = filtro == filtroAtual,
                        onClick = { onFiltroChange(filtro) },
                        label = { Text(filtro.rotulo) }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Todos", "Pequeno", "Médio", "Grande").forEach { porte ->
                    FilterChip(
                        selected = porteSelecionado.equals(porte, ignoreCase = true),
                        onClick = { porteSelecionado = porte },
                        label = { Text(porte) }
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Todas", "Filhote", "Adulto", "Idoso").forEach { faixa ->
                    FilterChip(
                        selected = idadeSelecionada == faixa,
                        onClick = { idadeSelecionada = faixa },
                        label = { Text(faixa) }
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = vacinadoSelecionado != null,
                    onClick = { vacinadoSelecionado = when (vacinadoSelecionado) { null -> true; true -> false; else -> null } },
                    label = { Text(if (vacinadoSelecionado == false) "Não vacinado" else "Vacinado") }
                )
                FilterChip(
                    selected = castradoSelecionado != null,
                    onClick = { castradoSelecionado = when (castradoSelecionado) { null -> true; true -> false; else -> null } },
                    label = { Text(if (castradoSelecionado == false) "Não castrado" else "Castrado") }
                )
            }

            val animaisVisiveis = if (estado is AnimalUiState.Sucesso) {
                estado.animais.filter { animal ->
                    val passaPorte = porteSelecionado == "Todos" || animal.porte.contains(porteSelecionado, ignoreCase = true)
                    val passaVacinacao = vacinadoSelecionado == null || animal.vacinado == vacinadoSelecionado
                    val passaCastracao = castradoSelecionado == null || animal.castrado == castradoSelecionado
                    val idade = animal.idade.lowercase()
                    val numeroIdade = Regex("\\d+").find(idade)?.value?.toIntOrNull()
                    val idadeEmMeses = idade.contains("mes") || idade.contains("mês")
                    val idadeEmAnos = idade.contains("ano") || idade.contains("anos")
                    val anosCalculados = when {
                        numeroIdade == null -> null
                        idadeEmMeses -> numeroIdade / 12
                        idadeEmAnos -> numeroIdade
                        else -> numeroIdade
                    }
                    val passaIdade = when (idadeSelecionada) {
                        "Filhote" -> idade.contains("filhote") || (idadeEmMeses && numeroIdade != null && numeroIdade < 12) || anosCalculados?.let { it < 1 } == true
                        "Adulto" -> idade.contains("adult") || anosCalculados?.let { it in 1..7 } == true
                        "Idoso" -> idade.contains("idos") || anosCalculados?.let { it >= 8 } == true
                        else -> true
                    }
                    passaPorte && passaVacinacao && passaCastracao && passaIdade
                }
            } else emptyList()
            val filtrosAvancadosAtivos = porteSelecionado != "Todos" || idadeSelecionada != "Todas" || vacinadoSelecionado != null || castradoSelecionado != null

            Spacer(modifier = Modifier.height(8.dp))

            when (estado) {

                is AnimalUiState.Carregando -> {
                    EstadoCarregando()
                }

                is AnimalUiState.Sucesso -> {

                    if (animaisVisiveis.isEmpty()) {

                        if (estado.filtrando || filtrosAvancadosAtivos) {
                            EstadoVazio(
                                titulo = "Nada por aqui",
                                mensagem = "Nenhum animal bate com a busca ou o filtro escolhido."
                            )
                        } else {
                            EstadoVazio(
                                titulo = "Nenhum animal cadastrado",
                                mensagem = "Toque no + para cadastrar o primeiro."
                            )
                        }

                    } else {

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 88.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            items(
                                items = animaisVisiveis,
                                key = { animal -> animal.id }
                            ) { animal ->

                                AnimalCard(
                                    animal = animal,
                                    onClick = { onAbrirDetalhes(animal.id) },
                                    onExcluir = { onExcluir(animal) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

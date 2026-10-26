package com.example.adotapet.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.adotapet.data.local.AnimalEntity
import com.example.adotapet.model.TipoAnimal
import com.example.adotapet.model.statusRotulo
import com.example.adotapet.ui.components.EstadoCarregando
import com.example.adotapet.ui.components.InfoLinha
import com.example.adotapet.viewmodel.AnimalUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalhesScreen(
    estado: AnimalUiState,
    animalId: Long,
    onVoltar: () -> Unit,
    onEditar: (Long) -> Unit,
    onFinalizar: (AnimalEntity) -> Unit,
    onExcluir: (AnimalEntity) -> Unit,
    onReabrir: (AnimalEntity) -> Unit,
    onInteresse: (Long) -> Unit,
    onIniciarAdocao: (AnimalEntity) -> Unit
) {

    val animal = if (estado is AnimalUiState.Sucesso) {
        estado.todos.find { it.id == animalId }
    } else {
        null
    }

    if (estado is AnimalUiState.Sucesso && animal == null) {
        LaunchedEffect(Unit) {
            onVoltar()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(animal?.nome ?: "Detalhes") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                actions = {
                    if (animal != null) {
                        IconButton(onClick = { onEditar(animal.id) }) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Editar"
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->

        if (animal == null) {
            EstadoCarregando(Modifier.padding(padding))
        } else {
            DetalhesConteudo(
                animal = animal,
                onFinalizar = { onFinalizar(animal) },
                onExcluir = { onExcluir(animal) },
                onReabrir = { onReabrir(animal) },
                onInteresse = { onInteresse(animal.id) },
                onIniciarAdocao = { onIniciarAdocao(animal) },
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun DetalhesConteudo(
    animal: AnimalEntity,
    onFinalizar: () -> Unit,
    onExcluir: () -> Unit,
    onReabrir: () -> Unit,
    onInteresse: () -> Unit,
    onIniciarAdocao: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Card(modifier = Modifier.fillMaxWidth()) {

            Column(modifier = Modifier.padding(16.dp)) {
                if (animal.fotoUrl.isNotBlank()) {
                    AsyncImage(
                        model = animal.fotoUrl,
                        contentDescription = "Foto de ${animal.nome}",
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Text(
                    text = animal.statusRotulo(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                InfoLinha("Espécie", animal.especie)
                InfoLinha("Idade", animal.idade)
                if (animal.tipo == TipoAnimal.ADOCAO) {
                    InfoLinha("Porte", animal.porte)
                    InfoLinha("Vacinado", if (animal.vacinado) "Sim" else "Não informado / não")
                    InfoLinha("Castrado", if (animal.castrado) "Sim" else "Não informado / não")
                } else {
                    InfoLinha("Data do último avistamento", animal.dataUltimoVisto)
                }
                if (animal.fotoUrl.isNotBlank()) InfoLinha("Foto", animal.fotoUrl)
                InfoLinha(
                    rotulo = if (animal.tipo == TipoAnimal.PERDIDO) {
                        "Visto pela última vez em"
                    } else {
                        "Localização"
                    },
                    valor = animal.local
                )
                InfoLinha("Descrição", animal.descricao)
                InfoLinha("Contato", animal.contato)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (animal.tipo == TipoAnimal.ADOCAO && !animal.finalizado) {
            OutlinedButton(onClick = onInteresse, modifier = Modifier.fillMaxWidth(), enabled = !animal.emProcessoAdocao) {
                Text(if (animal.emProcessoAdocao) "Adoção em andamento" else "Tenho interesse em adotar")
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (!animal.emProcessoAdocao) {
                OutlinedButton(onClick = onIniciarAdocao, modifier = Modifier.fillMaxWidth()) { Text("Marcar em processo de adoção") }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        if (animal.finalizado) {
            Button(
                onClick = onReabrir,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reabrir cadastro")
            }
        } else {
            Button(
                onClick = onFinalizar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (animal.tipo == TipoAnimal.ADOCAO) {
                        "Marcar como adotado"
                    } else {
                        "Marcar como encontrado"
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onExcluir,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Excluir cadastro")
        }
    }
}

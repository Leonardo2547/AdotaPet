package com.example.adotapet.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.adotapet.data.local.AnimalEntity
import com.example.adotapet.model.Metricas
import com.example.adotapet.model.resumo
import com.example.adotapet.model.statusRotulo
import com.example.adotapet.ui.components.EstadoCarregando
import com.example.adotapet.viewmodel.AnimalUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestaoScreen(
    estado: AnimalUiState,
    temaEscuro: Boolean,
    onAbrirDetalhes: (Long) -> Unit,
    onTemaChange: (Boolean) -> Unit,
    onLimparHistorico: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Gestão") })
        }
    ) { padding ->

        when (estado) {

            is AnimalUiState.Carregando -> {
                EstadoCarregando(Modifier.padding(padding))
            }

            is AnimalUiState.Sucesso -> {

                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    item {
                        PainelMetricas(estado.metricas)
                    }

                    item {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Tema escuro",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.weight(1f)
                                )
                                Switch(
                                    checked = temaEscuro,
                                    onCheckedChange = onTemaChange
                                )
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Histórico",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(
                                onClick = onLimparHistorico,
                                enabled = estado.historico.isNotEmpty()
                            ) {
                                Text("Limpar")
                            }
                        }
                    }

                    if (estado.historico.isEmpty()) {
                        item {
                            Text(
                                text = "Quando um animal for adotado ou encontrado, ele aparece aqui.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        items(
                            items = estado.historico,
                            key = { animal -> animal.id }
                        ) { animal ->
                            ItemHistorico(
                                animal = animal,
                                onClick = { onAbrirDetalhes(animal.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PainelMetricas(metricas: Metricas) {

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MetricaCard(metricas.paraAdocao, "Para adoção", Modifier.weight(1f))
            MetricaCard(metricas.perdidos, "Perdidos", Modifier.weight(1f))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MetricaCard(metricas.adotados, "Adotados", Modifier.weight(1f))
            MetricaCard(metricas.encontrados, "Encontrados", Modifier.weight(1f))
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Taxa de sucesso: ${(metricas.taxaDeSucesso * 100).toInt()}%",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { metricas.taxaDeSucesso },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun MetricaCard(
    valor: Int,
    rotulo: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = valor.toString(),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = rotulo,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ItemHistorico(
    animal: AnimalEntity,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "🐾 ${animal.nome}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = animal.resumo(),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = animal.statusRotulo(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

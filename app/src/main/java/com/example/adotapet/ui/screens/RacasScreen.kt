package com.example.adotapet.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.adotapet.ui.components.EstadoCarregando
import com.example.adotapet.ui.components.EstadoErro
import com.example.adotapet.viewmodel.RacasUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RacasScreen(
    estado: RacasUiState,
    onRecarregar: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Conheça as raças") },
                actions = {
                    IconButton(onClick = onRecarregar) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Carregar novas fotos"
                        )
                    }
                }
            )
        }
    ) { padding ->

        when (estado) {

            is RacasUiState.Carregando -> {
                EstadoCarregando(Modifier.padding(padding))
            }

            is RacasUiState.Erro -> {
                EstadoErro(
                    mensagem = estado.mensagem,
                    onTentarNovamente = onRecarregar,
                    modifier = Modifier.padding(padding)
                )
            }

            is RacasUiState.Sucesso -> {

                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    itemsIndexed(
                        items = estado.fotos,
                        key = { indice, foto -> "$indice-${foto.url}" }
                    ) { _, foto ->

                        Card(modifier = Modifier.fillMaxWidth()) {

                            Column {

                                AsyncImage(
                                    model = foto.url,
                                    contentDescription = foto.raca,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(220.dp)
                                )

                                Text(
                                    text = foto.raca,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

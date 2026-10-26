package com.example.adotapet.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.adotapet.data.local.AnimalEntity
import com.example.adotapet.model.TipoAnimal
import com.example.adotapet.ui.components.EstadoCarregando
import com.example.adotapet.ui.components.EstadoVazio
import com.example.adotapet.viewmodel.AnimalUiState

@Composable
fun FormScreen(
    estado: AnimalUiState,
    animalId: Long,
    onVoltar: () -> Unit,
    onSalvar: (AnimalEntity) -> Unit,
    onSolicitarDescarte: () -> Unit
) {

    val editando = animalId != -1L

    when (estado) {

        is AnimalUiState.Carregando -> {
            EstadoCarregando()
        }

        is AnimalUiState.Sucesso -> {

            val original = estado.todos.find { it.id == animalId }

            if (editando && original == null) {
                EstadoVazio(
                    titulo = "Animal não encontrado",
                    mensagem = "Esse cadastro pode ter sido removido."
                )
            } else {
                FormConteudo(
                    original = original,
                    onVoltar = onVoltar,
                    onSalvar = onSalvar,
                    onSolicitarDescarte = onSolicitarDescarte
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormConteudo(
    original: AnimalEntity?,
    onVoltar: () -> Unit,
    onSalvar: (AnimalEntity) -> Unit,
    onSolicitarDescarte: () -> Unit
) {

    var nome by rememberSaveable { mutableStateOf(original?.nome ?: "") }
    var especie by rememberSaveable { mutableStateOf(original?.especie ?: "") }
    var idade by rememberSaveable { mutableStateOf(original?.idade ?: "") }
    var tipoNome by rememberSaveable { mutableStateOf((original?.tipo ?: TipoAnimal.ADOCAO).name) }
    var local by rememberSaveable { mutableStateOf(original?.local ?: "") }
    var descricao by rememberSaveable { mutableStateOf(original?.descricao ?: "") }
    var contato by rememberSaveable { mutableStateOf(original?.contato ?: "") }
    var porte by rememberSaveable { mutableStateOf(original?.porte ?: "Não informado") }
    var vacinado by rememberSaveable { mutableStateOf(original?.vacinado ?: false) }
    var castrado by rememberSaveable { mutableStateOf(original?.castrado ?: false) }
    var dataUltimoVisto by rememberSaveable { mutableStateOf(original?.dataUltimoVisto ?: "") }
    var fotoUrl by rememberSaveable { mutableStateOf(original?.fotoUrl ?: "") }

    val tipo = TipoAnimal.valueOf(tipoNome)

    val alterado = nome != (original?.nome ?: "") ||
            especie != (original?.especie ?: "") ||
            idade != (original?.idade ?: "") ||
            tipo != (original?.tipo ?: TipoAnimal.ADOCAO) ||
            local != (original?.local ?: "") ||
            descricao != (original?.descricao ?: "") ||
            contato != (original?.contato ?: "") ||
            porte != (original?.porte ?: "Não informado") ||
            vacinado != (original?.vacinado ?: false) ||
            castrado != (original?.castrado ?: false) ||
            dataUltimoVisto != (original?.dataUltimoVisto ?: "") ||
            fotoUrl != (original?.fotoUrl ?: "")

    val podeSalvar = nome.isNotBlank() &&
            especie.isNotBlank() &&
            (tipo == TipoAnimal.ADOCAO || local.isNotBlank())

    BackHandler(enabled = alterado) {
        onSolicitarDescarte()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (original == null) "Novo animal" else "Editar animal")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (alterado) onSolicitarDescarte() else onVoltar()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            Text(
                text = "Tipo do cadastro",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TipoAnimal.entries.forEach { opcao ->
                    FilterChip(
                        selected = tipo == opcao,
                        onClick = { tipoNome = opcao.name },
                        label = { Text(opcao.rotulo) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = especie,
                onValueChange = { especie = it },
                label = { Text("Espécie (ex.: Cachorro, Gato)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (tipo == TipoAnimal.ADOCAO) {
                OutlinedTextField(
                    value = idade,
                    onValueChange = { idade = it },
                    label = { Text("Idade (opcional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            if (tipo == TipoAnimal.ADOCAO) {
                OutlinedTextField(
                    value = porte,
                    onValueChange = { porte = it },
                    label = { Text("Porte (pequeno, médio ou grande)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Vacinado")
                    Switch(checked = vacinado, onCheckedChange = { vacinado = it })
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Castrado")
                    Switch(checked = castrado, onCheckedChange = { castrado = it })
                }
                Spacer(modifier = Modifier.height(8.dp))
            } else {
                OutlinedTextField(
                    value = dataUltimoVisto,
                    onValueChange = { dataUltimoVisto = it },
                    label = { Text("Data do último avistamento (ex.: 08/10/2026)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            OutlinedTextField(
                value = fotoUrl,
                onValueChange = { fotoUrl = it },
                label = { Text("Link da foto (opcional)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = local,
                onValueChange = { local = it },
                label = {
                    Text(
                        if (tipo == TipoAnimal.PERDIDO) {
                            "Local onde foi visto"
                        } else {
                            "Localização (opcional)"
                        }
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição (opcional)") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = contato,
                onValueChange = { contato = it },
                label = { Text("Contato (opcional)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onSalvar(
                        AnimalEntity(
                            id = original?.id ?: 0,
                            nome = nome.trim(),
                            especie = especie.trim(),
                            idade = idade.trim(),
                            tipo = tipo,
                            local = local.trim(),
                            descricao = descricao.trim(),
                            contato = contato.trim(),
                            porte = porte.trim().ifBlank { "Não informado" },
                            vacinado = vacinado,
                            castrado = castrado,
                            dataUltimoVisto = dataUltimoVisto.trim(),
                            fotoUrl = fotoUrl.trim(),
                            finalizado = original?.finalizado ?: false,
                            emProcessoAdocao = original?.emProcessoAdocao ?: false,
                            criadoEm = original?.criadoEm ?: System.currentTimeMillis()
                        )
                    )
                },
                enabled = podeSalvar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (original == null) "Cadastrar animal" else "Salvar alterações")
            }
        }
    }
}

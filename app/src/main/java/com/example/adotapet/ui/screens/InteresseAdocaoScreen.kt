package com.example.adotapet.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.adotapet.data.local.AnimalEntity
import com.example.adotapet.data.local.InteresseAdocao

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteresseAdocaoScreen(
    animal: AnimalEntity,
    onVoltar: () -> Unit,
    onEnviar: (InteresseAdocao) -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var moradia by remember { mutableStateOf("") }
    var experiencia by remember { mutableStateOf("") }
    var enviado by remember { mutableStateOf(false) }
    Scaffold(topBar = {
        TopAppBar(title = { Text("Interesse em adoção") }, navigationIcon = {
            IconButton(onClick = onVoltar) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar") }
        })
    }) { padding ->
        Column(Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Quero adotar ${animal.nome}", style = MaterialTheme.typography.titleLarge)
            Text("Preencha os dados para registrar seu interesse. A equipe responsável poderá entrar em contato.")
            OutlinedTextField(nome, { nome = it }, label = { Text("Seu nome") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(telefone, { telefone = it }, label = { Text("Telefone para contato") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(moradia, { moradia = it }, label = { Text("Como é sua moradia?") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
            OutlinedTextField(experiencia, { experiencia = it }, label = { Text("Já teve animais? Como pretende cuidar?") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
            if (enviado) Text("Interesse registrado com sucesso.", color = MaterialTheme.colorScheme.primary)
            Button(onClick = {
                onEnviar(InteresseAdocao(animalId = animal.id, nomeInteressado = nome.trim(), telefone = telefone.trim(), moradia = moradia.trim(), experiencia = experiencia.trim()))
                enviado = true
            }, enabled = nome.isNotBlank() && telefone.isNotBlank() && moradia.isNotBlank() && experiencia.isNotBlank() && !enviado, modifier = Modifier.fillMaxWidth()) { Text("Enviar interesse") }
        }
    }
}

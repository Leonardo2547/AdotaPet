package com.example.adotapet.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.adotapet.ui.components.ConfirmacaoDialog
import com.example.adotapet.ui.screens.AnimaisScreen
import com.example.adotapet.ui.screens.DetalhesScreen
import com.example.adotapet.ui.screens.FormScreen
import com.example.adotapet.ui.screens.GestaoScreen
import com.example.adotapet.ui.screens.InteresseAdocaoScreen
import com.example.adotapet.data.local.InteresseAdocao
import com.example.adotapet.ui.screens.OnboardingScreen
import com.example.adotapet.ui.screens.RacasScreen
import com.example.adotapet.viewmodel.AnimalViewModel
import com.example.adotapet.viewmodel.RacasViewModel

private data class ItemNavegacao(
    val rota: String,
    val rotulo: String,
    val icone: ImageVector
)

private val itensNavegacao = listOf(
    ItemNavegacao(Rotas.ANIMAIS, "Animais", Icons.Filled.Favorite),
    ItemNavegacao(Rotas.GESTAO, "Gestão", Icons.AutoMirrored.Filled.List),
    ItemNavegacao(Rotas.RACAS, "Raças", Icons.Filled.Search)
)

@Composable
fun AdotaPetApp(
    factory: ViewModelProvider.Factory,
    onboardingConcluido: Boolean
) {

    val navController = rememberNavController()

    val viewModel: AnimalViewModel = viewModel(factory = factory)

    val estado by viewModel.uiState.collectAsState()
    val temaEscuro by viewModel.temaEscuro.collectAsState()
    val animalParaExcluir by viewModel.animalParaExcluir.collectAsState()
    val animalParaFinalizar by viewModel.animalParaFinalizar.collectAsState()
    val confirmandoLimpeza by viewModel.confirmandoLimpeza.collectAsState()
    val confirmandoDescarte by viewModel.confirmandoDescarte.collectAsState()

    val destinoInicial = remember {
        if (onboardingConcluido) Rotas.ANIMAIS else Rotas.ONBOARDING
    }

    val entradaAtual by navController.currentBackStackEntryAsState()
    val destinoAtual = entradaAtual?.destination
    val mostrarBarra = destinoAtual?.route in Rotas.principais

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (mostrarBarra) {
                BarraNavegacao(
                    destinoAtual = destinoAtual,
                    onNavegar = { rota ->
                        navController.navigate(rota) {
                            popUpTo(Rotas.ANIMAIS) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = destinoInicial,
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding)
        ) {

            composable(Rotas.ONBOARDING) {
                OnboardingScreen(
                    onComecar = {
                        viewModel.concluirOnboarding()
                        navController.navigate(Rotas.ANIMAIS) {
                            popUpTo(Rotas.ONBOARDING) { inclusive = true }
                        }
                    }
                )
            }

            composable(Rotas.ANIMAIS) {
                AnimaisScreen(
                    estado = estado,
                    onNovo = { navController.navigate(Rotas.formulario()) },
                    onAbrirDetalhes = { id -> navController.navigate(Rotas.detalhes(id)) },
                    onFiltroChange = viewModel::alterarFiltro,
                    onBuscaChange = viewModel::alterarBusca,
                    onExcluir = viewModel::solicitarExclusao
                )
            }

            composable(Rotas.GESTAO) {
                GestaoScreen(
                    estado = estado,
                    temaEscuro = temaEscuro,
                    onAbrirDetalhes = { id -> navController.navigate(Rotas.detalhes(id)) },
                    onTemaChange = viewModel::alterarTema,
                    onLimparHistorico = viewModel::solicitarLimpeza
                )
            }

            composable(Rotas.RACAS) {
                val racasViewModel: RacasViewModel = viewModel(factory = factory)
                val racasEstado by racasViewModel.uiState.collectAsState()

                RacasScreen(
                    estado = racasEstado,
                    onRecarregar = racasViewModel::carregar
                )
            }

            composable(
                route = Rotas.FORMULARIO,
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.LongType
                        defaultValue = -1L
                    }
                )
            ) { backStackEntry ->

                val id = backStackEntry.arguments?.getLong("id") ?: -1L

                FormScreen(
                    estado = estado,
                    animalId = id,
                    onVoltar = { navController.popBackStack() },
                    onSalvar = { animal ->
                        if (animal.id == 0L) {
                            viewModel.cadastrarAnimal(animal)
                        } else {
                            viewModel.atualizarAnimal(animal)
                        }
                        navController.popBackStack()
                    },
                    onSolicitarDescarte = viewModel::solicitarDescarte
                )
            }

            composable(
                route = Rotas.INTERESSE,
                arguments = listOf(navArgument("id") { type = NavType.LongType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getLong("id") ?: -1L
                val animal = (estado as? com.example.adotapet.viewmodel.AnimalUiState.Sucesso)?.todos?.find { it.id == id }
                if (animal != null) {
                    InteresseAdocaoScreen(
                        animal = animal,
                        onVoltar = { navController.popBackStack() },
                        onEnviar = { interesse -> viewModel.registrarInteresse(interesse) }
                    )
                }
            }

            composable(
                route = Rotas.DETALHES,
                arguments = listOf(
                    navArgument("id") { type = NavType.LongType }
                )
            ) { backStackEntry ->

                val id = backStackEntry.arguments?.getLong("id") ?: -1L

                DetalhesScreen(
                    estado = estado,
                    animalId = id,
                    onVoltar = { navController.popBackStack() },
                    onEditar = { animalId -> navController.navigate(Rotas.formulario(animalId)) },
                    onFinalizar = viewModel::solicitarFinalizacao,
                    onExcluir = viewModel::solicitarExclusao,
                    onReabrir = viewModel::reabrir,
                    onInteresse = { animalId -> navController.navigate(Rotas.interesse(animalId)) },
                    onIniciarAdocao = viewModel::iniciarProcessoAdocao
                )
            }
        }
    }

    animalParaExcluir?.let { animal ->
        ConfirmacaoDialog(
            titulo = "Excluir animal?",
            mensagem = "Tem certeza que deseja excluir ${animal.nome}? Essa ação não pode ser desfeita.",
            textoConfirmar = "Excluir",
            onConfirmar = viewModel::confirmarExclusao,
            onCancelar = viewModel::cancelarExclusao
        )
    }

    animalParaFinalizar?.let { animal ->
        ConfirmacaoDialog(
            titulo = "Finalizar cadastro?",
            mensagem = "${animal.nome} sai da lista principal e vai para o histórico.",
            textoConfirmar = "Confirmar",
            onConfirmar = viewModel::confirmarFinalizacao,
            onCancelar = viewModel::cancelarFinalizacao
        )
    }

    if (confirmandoLimpeza) {
        ConfirmacaoDialog(
            titulo = "Limpar histórico?",
            mensagem = "Todos os animais já adotados ou encontrados serão apagados de vez.",
            textoConfirmar = "Limpar",
            onConfirmar = viewModel::confirmarLimpeza,
            onCancelar = viewModel::cancelarLimpeza
        )
    }

    if (confirmandoDescarte) {
        ConfirmacaoDialog(
            titulo = "Descartar alterações?",
            mensagem = "O que você preencheu ainda não foi salvo e será perdido.",
            textoConfirmar = "Descartar",
            textoCancelar = "Continuar editando",
            onConfirmar = {
                viewModel.fecharDescarte()
                navController.popBackStack()
            },
            onCancelar = viewModel::fecharDescarte
        )
    }
}

@Composable
private fun BarraNavegacao(
    destinoAtual: NavDestination?,
    onNavegar: (String) -> Unit
) {
    NavigationBar {
        itensNavegacao.forEach { item ->
            NavigationBarItem(
                selected = destinoAtual?.hierarchy?.any { it.route == item.rota } == true,
                onClick = { onNavegar(item.rota) },
                icon = { Icon(item.icone, contentDescription = null) },
                label = { Text(item.rotulo) }
            )
        }
    }
}

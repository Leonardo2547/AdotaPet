package com.example.adotapet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.adotapet.ui.navigation.AdotaPetApp
import com.example.adotapet.ui.theme.AdotaPetTheme
import com.example.adotapet.viewmodel.AnimalViewModel
import com.example.adotapet.viewmodel.AppViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val factory = AppViewModelFactory(applicationContext)

        setContent {

            val viewModel: AnimalViewModel = viewModel(factory = factory)

            val temaEscuro by viewModel.temaEscuro.collectAsState()
            val onboardingConcluido by viewModel.onboardingConcluido.collectAsState()

            AdotaPetTheme(darkTheme = temaEscuro) {

                Surface(modifier = Modifier.fillMaxSize()) {

                    onboardingConcluido?.let { concluido ->
                        AdotaPetApp(
                            factory = factory,
                            onboardingConcluido = concluido
                        )
                    }
                }
            }
        }
    }
}

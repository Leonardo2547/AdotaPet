package com.example.adotapet

import com.example.adotapet.data.local.AnimalEntity
import com.example.adotapet.model.FiltroAnimal
import com.example.adotapet.model.TipoAnimal
import com.example.adotapet.model.filtrar
import com.example.adotapet.model.metricas
import com.example.adotapet.model.nomeDaRaca
import com.example.adotapet.model.resumo
import org.junit.Assert.assertEquals
import org.junit.Test

class AnimalExtensoesTest {

    private val rex = AnimalEntity(
        id = 1,
        nome = "Rex",
        especie = "Cachorro",
        idade = "2 anos",
        tipo = TipoAnimal.ADOCAO
    )

    private val mel = AnimalEntity(
        id = 2,
        nome = "Mel",
        especie = "Cachorro",
        tipo = TipoAnimal.PERDIDO,
        local = "Centro"
    )

    private val luna = AnimalEntity(
        id = 3,
        nome = "Luna",
        especie = "Gato",
        tipo = TipoAnimal.ADOCAO,
        finalizado = true
    )

    @Test
    fun filtrarPorTipo() {
        assertEquals(listOf(rex), listOf(rex, mel).filtrar(FiltroAnimal.ADOCAO, ""))
        assertEquals(listOf(mel), listOf(rex, mel).filtrar(FiltroAnimal.PERDIDO, ""))
    }

    @Test
    fun filtrarPorBuscaIgnorandoMaiusculas() {
        assertEquals(listOf(mel), listOf(rex, mel).filtrar(FiltroAnimal.TODOS, "centro"))
    }

    @Test
    fun metricasSeparamAtivosEFinalizados() {
        val resultado = listOf(rex, mel, luna).metricas()
        assertEquals(2, resultado.ativos)
        assertEquals(1, resultado.paraAdocao)
        assertEquals(1, resultado.perdidos)
        assertEquals(1, resultado.adotados)
        assertEquals(1f / 3f, resultado.taxaDeSucesso, 0.001f)
    }

    @Test
    fun resumoDependeDoTipo() {
        assertEquals("Cachorro • 2 anos", rex.resumo())
        assertEquals("Cachorro • visto em Centro", mel.resumo())
    }

    @Test
    fun nomeDaRacaInverteRacaESubraca() {
        val url = "https://images.dog.ceo/breeds/hound-afghan/n02088094_1003.jpg"
        assertEquals("Afghan Hound", url.nomeDaRaca())
        assertEquals("Raça desconhecida", "https://exemplo.com/foto.jpg".nomeDaRaca())
    }
}

package com.example.adotapet.model

enum class TipoAnimal(val rotulo: String) {
    ADOCAO("Para adoção"),
    PERDIDO("Perdido")
}

enum class FiltroAnimal(val rotulo: String) {
    TODOS("Todos"),
    ADOCAO("Adoção"),
    PERDIDO("Perdidos")
}

data class Metricas(
    val ativos: Int = 0,
    val paraAdocao: Int = 0,
    val perdidos: Int = 0,
    val adotados: Int = 0,
    val encontrados: Int = 0
) {
    val finalizados: Int get() = adotados + encontrados

    val taxaDeSucesso: Float
        get() {
            val total = ativos + finalizados
            return if (total == 0) 0f else finalizados.toFloat() / total
        }
}

data class DogFoto(
    val url: String,
    val raca: String
)

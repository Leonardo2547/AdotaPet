package com.example.adotapet.model

import com.example.adotapet.data.local.AnimalEntity

fun List<AnimalEntity>.filtrar(filtro: FiltroAnimal, busca: String): List<AnimalEntity> {
    val termo = busca.trim()
    return filter { animal ->
        val passaNoFiltro = when (filtro) {
            FiltroAnimal.TODOS -> true
            FiltroAnimal.ADOCAO -> animal.tipo == TipoAnimal.ADOCAO
            FiltroAnimal.PERDIDO -> animal.tipo == TipoAnimal.PERDIDO
        }
        val passaNaBusca = termo.isEmpty() ||
                animal.nome.contains(termo, ignoreCase = true) ||
                animal.especie.contains(termo, ignoreCase = true) ||
                animal.local.contains(termo, ignoreCase = true) ||
                animal.porte.contains(termo, ignoreCase = true) ||
                animal.dataUltimoVisto.contains(termo, ignoreCase = true) ||
                animal.descricao.contains(termo, ignoreCase = true)
        passaNoFiltro && passaNaBusca
    }
}

fun List<AnimalEntity>.metricas(): Metricas {
    val ativos = filter { !it.finalizado }
    val finalizados = filter { it.finalizado }
    return Metricas(
        ativos = ativos.size,
        paraAdocao = ativos.count { it.tipo == TipoAnimal.ADOCAO },
        perdidos = ativos.count { it.tipo == TipoAnimal.PERDIDO },
        adotados = finalizados.count { it.tipo == TipoAnimal.ADOCAO },
        encontrados = finalizados.count { it.tipo == TipoAnimal.PERDIDO }
    )
}

fun AnimalEntity.resumo(): String {
    val partes = buildList {
        add(especie)
        if (tipo == TipoAnimal.ADOCAO && idade.isNotBlank()) add(idade)
        if (tipo == TipoAnimal.PERDIDO && local.isNotBlank()) add("visto em $local")
    }
    return partes.joinToString(" • ")
}

fun AnimalEntity.statusRotulo(): String = when {
    emProcessoAdocao && !finalizado -> "Em processo de adoção"
    finalizado && tipo == TipoAnimal.ADOCAO -> "Adotado"
    finalizado -> "Encontrado"
    tipo == TipoAnimal.ADOCAO -> "Disponível para adoção"
    else -> "Procurando o dono"
}

fun String.nomeDaRaca(): String =
    substringAfter("/breeds/", "")
        .substringBefore("/")
        .split("-")
        .filter { it.isNotBlank() }
        .reversed()
        .joinToString(" ") { parte -> parte.replaceFirstChar { it.uppercase() } }
        .ifBlank { "Raça desconhecida" }

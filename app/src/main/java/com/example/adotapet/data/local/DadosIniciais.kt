package com.example.adotapet.data.local

import com.example.adotapet.model.TipoAnimal

object DadosIniciais {

    val animais: List<AnimalEntity> = listOf(
        AnimalEntity(
            nome = "Rex",
            especie = "Cachorro",
            idade = "2 anos",
            tipo = TipoAnimal.ADOCAO,
            descricao = "Brincalhão, vacinado e muito sociável com crianças.",
            contato = "(00) 90000-0001"
        ),
        AnimalEntity(
            nome = "Luna",
            especie = "Gato",
            idade = "1 ano",
            tipo = TipoAnimal.ADOCAO,
            descricao = "Calma e carinhosa. Já é castrada.",
            contato = "(00) 90000-0002"
        ),
        AnimalEntity(
            nome = "Thor",
            especie = "Cachorro",
            idade = "3 anos",
            tipo = TipoAnimal.ADOCAO,
            descricao = "Porte grande, cheio de energia e precisa de espaço para correr.",
            contato = "(00) 90000-0003"
        ),
        AnimalEntity(
            nome = "Mel",
            especie = "Cachorro",
            tipo = TipoAnimal.PERDIDO,
            local = "Centro",
            descricao = "Pelagem caramelo, usa coleira azul.",
            contato = "(00) 90000-0004"
        ),
        AnimalEntity(
            nome = "Nina",
            especie = "Gato",
            tipo = TipoAnimal.PERDIDO,
            local = "Vila Nova",
            descricao = "Gata preta com patinhas brancas.",
            contato = "(00) 90000-0005"
        ),
        AnimalEntity(
            nome = "Bob",
            especie = "Cachorro",
            tipo = TipoAnimal.PERDIDO,
            local = "Jardim América",
            descricao = "Vira-lata pequeno, atende pelo nome.",
            contato = "(00) 90000-0006"
        )
    )
}

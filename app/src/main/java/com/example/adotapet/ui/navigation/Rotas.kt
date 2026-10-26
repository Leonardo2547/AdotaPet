package com.example.adotapet.ui.navigation

object Rotas {
    const val ONBOARDING = "onboarding"
    const val ANIMAIS = "animais"
    const val GESTAO = "gestao"
    const val RACAS = "racas"
    const val FORMULARIO = "formulario?id={id}"
    const val DETALHES = "detalhes/{id}"
    const val INTERESSE = "interesse/{id}"

    val principais = listOf(ANIMAIS, GESTAO, RACAS)

    fun formulario(id: Long? = null): String {
        return if (id == null) "formulario" else "formulario?id=$id"
    }

    fun interesse(id: Long): String = "interesse/$id"

    fun detalhes(id: Long): String {
        return "detalhes/$id"
    }
}

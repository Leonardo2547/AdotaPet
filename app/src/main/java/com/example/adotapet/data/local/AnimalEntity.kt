package com.example.adotapet.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.adotapet.model.TipoAnimal

@Entity(tableName = "animais")
data class AnimalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String,
    val especie: String,
    val idade: String = "",
    val tipo: TipoAnimal,
    val local: String = "",
    val descricao: String = "",
    val contato: String = "",
    val porte: String = "Não informado",
    val vacinado: Boolean = false,
    val castrado: Boolean = false,
    val dataUltimoVisto: String = "",
    val fotoUrl: String = "",
    val finalizado: Boolean = false,
    val emProcessoAdocao: Boolean = false,
    val criadoEm: Long = System.currentTimeMillis()
)

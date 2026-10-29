package com.example.myapplicationprimer

data class Movimentacao(
    val id: Long = 0,
    val descricao: String,
    val valor: Double,
    val tipo: String
)
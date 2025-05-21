package com.example.masterapp2.model

data class Carro(
    val id: Int = 0,
    val nome: String,
    val marca: String,
    val ano: Int
){

    override fun toString(): String {
        return nome
    }
}

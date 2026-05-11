package com.albin.demo.data

import kotlinx.serialization.Serializable
@Serializable
data class Comida(
    val nombre: String,
    val descripcion:String,
    val precio: Int
)
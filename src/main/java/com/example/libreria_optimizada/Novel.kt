package com.example.libreria_optimizada

data class Novel(
    val title: String,
    val year: Int,
    val author: String
){constructor() : this("", 0, "Anonimo")}


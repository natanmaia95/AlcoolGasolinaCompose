package com.example.alcoolgasolinacompose

data class GasStation(
    val id: Int,
    val name: String,
    val latidute: String,
    val longitude: String,
    val priceGasoline: Float,
    val priceAlcohol: Float
)
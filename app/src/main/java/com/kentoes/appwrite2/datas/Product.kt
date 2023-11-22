package com.kentoes.appwrite2.datas

data class Product(
    val name: String,
    val sku: String,
    val description: String,
    val price: Double,
    val imageUrl: String
)
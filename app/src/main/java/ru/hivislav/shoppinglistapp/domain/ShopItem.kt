package ru.hivislav.shoppinglistapp.domain

data class ShopItem(
    val id: Int,
    val name: String,
    val count: Int,
    val enabled: Boolean
)

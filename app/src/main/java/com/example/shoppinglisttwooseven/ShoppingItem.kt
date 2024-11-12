package com.example.shoppinglisttwooseven

data class ShoppingItem(
    val id: Int,
    var title: String,
    var description: String,
    var quantity: Int,
    var isEditing: Boolean = false,
    var location:Location?
)
data class Location(
    val latitude: Double,
    val longitude: Double,
    val address: String = "",
)


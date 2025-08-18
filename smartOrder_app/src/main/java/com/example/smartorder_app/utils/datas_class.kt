package com.example.smartorder_app.utils

data class UserConfig(
    val name: Name,
    val email: String,
    val cellphone: Cellphone,
    val role: String,
    val token: String,
    var isLoggedIn: Boolean = false
)

data class Name(
    val name: String,
    val fatherName: String,
    val motherName: String
)

data class Cellphone(
    val countryCode: String,
    val number: String
)

data class RestaurantData(
    val id: String,
    val name: String,
    val description: String,
    val owner: String,
    val contact: ContactRestaurant,
    val imageUrl: String,
    val logoUrl: String,
    val images: List<String>,
    val WorkingDays: List<WorkingDays>,
    val rating: Float,
    val isFavorite: Boolean,
    val category: List<String>,
    val maxCapacity: Int,
    val currentCapacity: Int,
    val state: String,
    val status: String,
    val foods: List<String>
)

data class ContactRestaurant (
    val email: String,
    val countryCode: String,
    val number: String
)

data class WorkingDays (
    val day: String,
    val open: String,
    val close: String,
)

data class FoodData (
    val name: String,
    val restaurant: String,
    val description: String,
    val price: Number,
    val sales: Number,
    val category: String,
    val images: List<String>,
    val date: Date
)

data class Date (
    val date: String,
    val time: String
)

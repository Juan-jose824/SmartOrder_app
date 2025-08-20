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
    var isFavorite: Boolean,
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
    val category: List<String>,
    val images: List<String>,
    val date: Date,
)

data class Date (
    val date: String,
    val time: String
)

data class FoodDetails (
    val name: String,
    val restaurant: String,
    val prices: PricesData,
    val color: String,
    val details: String
)

data class PricesData (
    val price: Number,
    val date: String
)

data class CartItem(
    val food: FoodData,
    var quantity: Int
) {
    fun getTotalPrice(): Double {
        return food.price.toDouble() * quantity
    }
}

data class PlaceData(
    val id: String,
    val restaurant: String,
    val name: String,
    val description: String,
    val images: List<String>,
    val status: String
)

data class AppConfig (
    val user: String,
    val theme: String = "Dark",
    val reservations: Boolean = true,
    val publicWorkTime: Boolean = true,
    val publicContact: Boolean = true,
    val publicDescription: Boolean = true,
    val notifications: Boolean = true,
    val alerts: Boolean = true,
    val onlyRestaurant: Boolean = false,
    val reports: Boolean = false,
    val grafics: Boolean = false,
    val adminUsers: Boolean = false,
    val adminFoods: Boolean = false,
    val adminCategories: Boolean = false,
    val adminPlaces: Boolean = false,
    val adminReservations: Boolean = false,
)

data class ReservationData (
    val restaurant: String,
    val customer: String,
    val site: String,
    val people: Number,
    val items: List<ReservationItem>,
    val status: String,
    val time: Time,
    val date: Date,
    val price: Number,
    val totalPrice: Number
)

data class Time (
    val day: String,
    val time: String,
)

data class ReservationItem(
    val food: String,
    val quantity: Int,
    val price: Double
)
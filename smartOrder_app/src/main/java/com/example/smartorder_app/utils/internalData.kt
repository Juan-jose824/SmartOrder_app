package com.example.smartorder_app.utils

import android.content.Context
import com.google.gson.Gson

class InternalData(private val context: Context) {
    private val gson = Gson()
    private val fileName = "config.json"

    fun save(config: UserConfig) {
        val jsonString = gson.toJson(config)
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
        }
    }

    fun getData(): UserConfig? {
        return try {
            val json = context.openFileInput(fileName).bufferedReader().use { it.readText() }
            gson.fromJson(json, UserConfig::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

object CartManager {
    private val cartItems = mutableListOf<CartItem>()

    fun addToCart(food: FoodData, quantity: Int = 1) {
        val existing = cartItems.find { it.food.name == food.name }
        if (existing != null) {
            existing.quantity += quantity
        } else {
            cartItems.add(CartItem(food, quantity))
        }
    }

    fun getCart(): MutableList<CartItem> = cartItems
}

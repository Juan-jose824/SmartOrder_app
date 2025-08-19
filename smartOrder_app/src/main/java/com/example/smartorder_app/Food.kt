package com.example.smartorder_app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.smartorder_app.utils.CartItem
import com.example.smartorder_app.utils.CartManager
import com.example.smartorder_app.utils.Date
import com.example.smartorder_app.utils.FoodData

class Food : ComponentActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.food)

        // Referencias
        val foodName = findViewById<TextView>(R.id.foodName)
        val foodPrice = findViewById<TextView>(R.id.foodPrice)
        val foodDescription = findViewById<TextView>(R.id.foodDescription)
        val quantityText = findViewById<TextView>(R.id.quantityText)
        val btnIncrease = findViewById<Button>(R.id.btnIncrease)
        val btnDecrease = findViewById<Button>(R.id.btnDecrease)
        val addButton = findViewById<com.google.android.material.button.MaterialButton>(R.id.addButton)
        val btnCart: Button = findViewById(R.id.payButton)

        // Recibir datos del intent
        val name = intent.getStringExtra("food_name") ?: ""
        val restaurant = intent.getStringExtra("food_restaurant") ?: ""
        val price = intent.getDoubleExtra("food_price", 0.0)
        val description = intent.getStringExtra("food_description") ?: ""

        // Construir el objeto FoodData con los datos recibidos
        val foodData = FoodData(
            name = name,
            restaurant = restaurant,
            description = description,
            price = price,
            sales = 0, // o lo que recibas
            category = "Sin categoría", // ajusta según tu caso
            images = emptyList(), // si no mandas imágenes por el intent
            date = Date("2025-08-18", "16:00") // o el valor real que quieras
        )

        // Mostrar
        foodName.text = "$name - $restaurant"
        foodPrice.text = "$$price"
        foodDescription.text = description

        // Manejar cantidad
        var quantity = 1
        quantityText.text = quantity.toString()

        btnIncrease.setOnClickListener {
            quantity++
            quantityText.text = quantity.toString()
        }

        btnDecrease.setOnClickListener {
            if (quantity > 1) {
                quantity--
                quantityText.text = quantity.toString()
            }
        }

        btnCart.setOnClickListener {
            val intent = Intent(this, Cart::class.java)
            startActivity(intent)
        }

        // Botón Añadir al carrito
        addButton.setOnClickListener {
            CartManager.addToCart(foodData, quantity)
            Toast.makeText(this, "Añadido al carrito", Toast.LENGTH_SHORT).show()
        }

    }
}
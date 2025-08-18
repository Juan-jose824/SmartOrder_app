package com.example.smartorder_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class Food : ComponentActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.food)

        // Obtener referencias de la UI
        val foodImage = findViewById<ImageView>(R.id.foodImage)
        val foodName = findViewById<TextView>(R.id.foodName)
        val foodPrice = findViewById<TextView>(R.id.foodPrice)
        val foodDescription = findViewById<TextView>(R.id.foodDescription)

        // Recibir datos desde el intent
        val name = intent.getStringExtra("food_name")
        val restaurant = intent.getStringExtra("food_restaurant")
        val price = intent.getDoubleExtra("food_price", 0.0)
        val description = intent.getStringExtra("food_description")

        // Asignar a la vista
        foodName.text = "$name - $restaurant"
        foodPrice.text = "$$price"
        foodDescription.text = description
    }

}
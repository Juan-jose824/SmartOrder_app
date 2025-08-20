package com.example.smartorder_app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.bumptech.glide.Glide
import com.example.smartorder_app.Services.getFood
import com.example.smartorder_app.utils.CartItem
import com.example.smartorder_app.utils.CartManager
import com.example.smartorder_app.utils.Date
import com.example.smartorder_app.utils.FoodData
import com.example.smartorder_app.utils.FoodDetails
import com.example.smartorder_app.utils.PricesData
import org.json.JSONObject

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

        var foodCart: FoodData? = null

        // Llamar a la API para obtener los detalles reales
        getFood(restaurant, name) { success, response ->
            if (success && response != null) {
                try {
                    val json = JSONObject(response)

                    // Parte "data"
                    val data = json.getJSONObject("data")
                    val dateObj = data.getJSONObject("date")

                    val categories = mutableListOf<String>()
                    val categoriesArray = data.optJSONArray("category")
                    if (categoriesArray != null) {
                        for (i in 0 until categoriesArray.length()) {
                            categories.add(categoriesArray.getString(i))
                        }
                    }

                    val images = mutableListOf<String>()
                    val imagesArray = data.optJSONArray("images")
                    if (imagesArray != null) {
                        for (i in 0 until imagesArray.length()) {
                            images.add(imagesArray.getString(i))
                        }
                    }

                    val foodData = FoodData(
                        name = data.getString("name"),
                        restaurant = data.getString("restaurant"),
                        description = data.getString("description"),
                        price = data.getDouble("price"),
                        sales = data.getInt("sales"),
                        category = categories,
                        images = images,
                        date = Date(
                            dateObj.getString("date"),
                            dateObj.getString("time")
                        )
                    )
                    foodCart = foodData

                    // Parte "details"
                    val details = json.getJSONObject("details")
                    val pricesArray = details.getJSONArray("prices")

                    val pricesList = mutableListOf<PricesData>()
                    for (i in 0 until pricesArray.length()) {
                        val p = pricesArray.getJSONObject(i)
                        pricesList.add(
                            PricesData(
                                price = p.getDouble("price"),
                                date = p.getString("date")
                            )
                        )
                    }

                    val foodDetails = FoodDetails(
                        name = details.getString("name"),
                        restaurant = details.getString("restaurant"),
                        prices = pricesList.first(),
                        color = details.getString("color"),
                        details = details.getString("details")
                    )

                    runOnUiThread {
                        foodName.text = foodData.name
                        foodPrice.text = "$${foodData.price}"
                        foodDescription.text = "${foodData.description}\n\n${foodDetails.details}"

                        // Si quieres mostrar imagen
                        if (foodData.images.isNotEmpty()) {
                            val foodImage = findViewById<ImageView>(R.id.foodImage)
                            Glide.with(this)
                                .load(foodData.images[0])
                                .placeholder(R.drawable.no_image)
                                .into(foodImage)
                        }
                    }

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

                } catch (e: Exception) {
                    Log.e("FoodActivity", "Error parseando: ${e.message}")
                }
            } else {
                runOnUiThread {
                    Toast.makeText(this, "Error al cargar alimento", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}
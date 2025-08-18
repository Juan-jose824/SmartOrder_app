package com.example.smartorder_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartorder_app.utils.Date
import com.example.smartorder_app.utils.FoodAdapter
import com.example.smartorder_app.utils.FoodData

class Restaurant : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FoodAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.menu)

        recyclerView = findViewById(R.id.allFoods_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val name = intent.getStringExtra("restaurant_name")
        val id = intent.getStringExtra("restaurant_id")

        Log.i("Restaruate", "$name $id")

        // Ejemplo: mostrar el nombre en un TextView
        val titleView: TextView = findViewById(R.id.restaurant_name)
        titleView.text = name ?: "Restaurante"

        val sampleFoods = listOf(
            FoodData(
                name = "Docena Variada",
                restaurant = "Krispy Kreme",
                description = "Elige tus sabores de Donas preferidos. La Docena perfecta para compartir.",
                price = 420,
                sales = 85,
                category = "Postres",
                images = listOf("https://cdn.ubereats.com/donas.jpg"),
                date = Date("2025-08-18", "16:00")
            ),
            FoodData(
                name = "Krispy Bites",
                restaurant = "Krispy Kreme",
                description = "Bocaditos con el inigualable sabor a Dona Glaseada Original®",
                price = 90,
                sales = 92,
                category = "Snacks",
                images = listOf("https://cdn.ubereats.com/bites.jpg"),
                date = Date("2025-08-18", "16:10")
            )
        )

        adapter = FoodAdapter(sampleFoods) { food ->
            Toast.makeText(this, "Seleccionaste ${food.name}", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = adapter
    }

}
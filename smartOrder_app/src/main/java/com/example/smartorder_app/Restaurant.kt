package com.example.smartorder_app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartorder_app.Services.getRestaurant
import com.example.smartorder_app.utils.Date
import com.example.smartorder_app.utils.FoodAdapter
import com.example.smartorder_app.utils.FoodData
import com.example.smartorder_app.utils.WorkingDays
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject

class Restaurant : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FoodAdapter
    private var foods = mutableListOf<FoodData>()

    private var isFavorite: Boolean = false

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.menu)

        recyclerView = findViewById(R.id.allFoods_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val name = intent.getStringExtra("restaurant_name")
        val id = intent.getStringExtra("restaurant_id")
        val favIcon: ImageView = findViewById(R.id.fav_icon)

        val btnCart: FloatingActionButton = findViewById(R.id.btnCart)

        favIcon.setOnClickListener {
            isFavorite = !isFavorite
            favIcon.setImageResource(
                if (isFavorite) R.drawable.heart else R.drawable.favorite
            )

            Toast.makeText(
                this,
                if (isFavorite) "$name añadido a favoritos"
                else "$name eliminado de favoritos",
                Toast.LENGTH_SHORT
            ).show()
        }

        btnCart.setOnClickListener {
            val intent = Intent(this, Cart::class.java)
            startActivity(intent)
        }

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
        adapter = FoodAdapter(sampleFoods)

        recyclerView.adapter = adapter

        id?.let { restaurantId ->
            getRestaurant(restaurantId) { success, data ->
                if (success && data != null) {
                    try {
                        val json = JSONObject(data)

                        // extraer info general del restaurante
                        val dataObj = json.getJSONObject("data")
                        val id = dataObj.optString("id")
                        val name = dataObj.optString("name")
                        val description = dataObj.optString("description")
                        val categoryArray = dataObj.optJSONArray("category")
                        val categories = mutableListOf<String>()
                        for (i in 0 until (categoryArray?.length() ?: 0)) {
                            categories.add(categoryArray!!.getString(i))
                        }

                        // detalles extra como horarios
                        val dataDetails = json.getJSONObject("dataDetails")
                        val workingDaysArray = dataDetails.optJSONArray("workingDays")

                        val workingDays = mutableListOf<WorkingDays>()
                        if (workingDaysArray != null) {
                            for (i in 0 until workingDaysArray.length()) {
                                val wd = workingDaysArray.getJSONObject(i)
                                workingDays.add(
                                    WorkingDays(
                                        day = wd.optString("day"),
                                        open = wd.optString("open"),
                                        close = wd.optString("close")
                                    )
                                )
                            }
                        }

                        runOnUiThread {
                            // 👇 Aquí actualizas la UI
                            findViewById<TextView>(R.id.restaurant_name).text = name
                            findViewById<TextView>(R.id.restaurant_description).text = description

                            // ejemplo: llenar horarios
                            setWorkingHours(workingDays)

                            Toast.makeText(this, "Restaurante cargado", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Log.e("Restaurant", "Error parseando JSON: ${e.message}")
                        runOnUiThread {
                            Toast.makeText(this, "Error parseando datos", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Error cargando restaurante", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    private fun setWorkingHours(workingDays: List<WorkingDays>) {
        // Referencias a tus TextViews
        val sunday = findViewById<TextView>(R.id.sundayTime)
        val monday = findViewById<TextView>(R.id.mondayTime)
        val tuesday = findViewById<TextView>(R.id.TuesdayTime)
        val wednesday = findViewById<TextView>(R.id.wednesdayTime)
        val thursday = findViewById<TextView>(R.id.thursdayTime)
        val friday = findViewById<TextView>(R.id.fridayTime)
        val saturday = findViewById<TextView>(R.id.saturndayTime)

        // Mapeo día → TextView
        val dayViews = mapOf(
            "Domingo" to sunday,
            "Lunes" to monday,
            "Martes" to tuesday,
            "Miercoles" to wednesday,
            "Jueves" to thursday,
            "Viernes" to friday,
            "Sabado" to saturday
        )

        // Primero poner "Sin datos" en todos
        dayViews.values.forEach { it.text = "Sin datos" }

        // Rellenar con lo que venga en la BD
        for (day in workingDays) {
            val view = dayViews[day.day]
            if (view != null) {
                view.text = "${day.open} - ${day.close}"
            }
        }
    }
}
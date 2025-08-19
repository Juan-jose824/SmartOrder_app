package com.example.smartorder_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartorder_app.utils.PlaceAdapter
import com.example.smartorder_app.utils.PlaceData

class Reservation : AppCompatActivity() {

    private lateinit var recyclerCart: RecyclerView
    private lateinit var placeAdapter: PlaceAdapter
    private val places = mutableListOf<PlaceData>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.reservation)

        recyclerCart = findViewById(R.id.recyclerCart)

        val spacing = 16 // dp

        recyclerCart.addItemDecoration(
            object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: android.graphics.Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.right = spacing
                }
            }
        )

        // mock data para probar
        places.add(
            PlaceData(
                id = "1",
                restaurant = "Pizza Hut",
                name = "Pizza Familiar",
                description = "Pizza grande con queso y peperoni",
                images = listOf("https://via.placeholder.com/150"),
                status = "Disponible"
            )
        )
        places.add(
            PlaceData(
                id = "2",
                restaurant = "Burger King",
                name = "Whopper",
                description = "Hamburguesa con doble carne",
                images = emptyList(),
                status = "Disponible"
            )
        )

        // inicializar adapter con callback
        placeAdapter = PlaceAdapter(places) { place, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "${place.name} añadido", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "${place.name} removido", Toast.LENGTH_SHORT).show()
            }
        }

        recyclerCart.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            true
        )
        recyclerCart.adapter = placeAdapter

    }
}
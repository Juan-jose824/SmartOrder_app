package com.example.smartorder_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartorder_app.utils.ContactRestaurant
import com.example.smartorder_app.utils.RestaurantAdapter
import com.example.smartorder_app.utils.RestaurantData
import com.example.smartorder_app.utils.WorkingDays

class History_Fragment  : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: RestaurantAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del fragment
        val view = inflater.inflate(R.layout.history_fragment, container, false)

        recycler = view.findViewById(R.id.recyclerHistory)
        recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // Datos de ejemplo
        val restaurants = listOf(
            RestaurantData(
                id = "1",
                name = "Pizza Loca",
                description = "Las mejores pizzas",
                owner = "Juan",
                contact = ContactRestaurant("pizza@loca.com", "+34", "123456789"),
                imageUrl = "https://via.placeholder.com/600x300",
                logoUrl = "https://via.placeholder.com/100",
                images = listOf(),
                WorkingDays = listOf(
                    WorkingDays("Lunes", "09:00", "22:00")
                ),
                rating = 4.5f,
                isFavorite = false,
                category = listOf("Pizza", "Comida rápida"),
                maxCapacity = 50,
                currentCapacity = 20,
                state = "available",
                status = "active",
                foods = listOf("Pizza Margarita", "Pizza Pepperoni")
            ),
            RestaurantData(
                id = "2",
                name = "Tortas Juan",
                description = "Las mejores pizzas",
                owner = "Juan",
                contact = ContactRestaurant("pizza@loca.com", "+34", "123456789"),
                imageUrl = "https://via.placeholder.com/600x300",
                logoUrl = "https://via.placeholder.com/100",
                images = listOf(),
                WorkingDays = listOf(
                    WorkingDays("Lunes", "09:00", "22:00")
                ),
                rating = 4.5f,
                isFavorite = false,
                category = listOf("Pizza", "Comida rápida"),
                maxCapacity = 50,
                currentCapacity = 20,
                state = "available",
                status = "active",
                foods = listOf("Pizza Margarita", "Pizza Pepperoni")
            )
        )

        adapter = RestaurantAdapter(restaurants) { restaurant ->
            Toast.makeText(requireContext(), "${restaurant.name} favorito!", Toast.LENGTH_SHORT).show()
        }

        recycler.adapter = adapter

        return view
    }
}
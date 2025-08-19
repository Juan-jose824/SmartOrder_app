package com.example.smartorder_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartorder_app.Services.getAllRestaurants
import com.example.smartorder_app.utils.ContactRestaurant
import com.example.smartorder_app.utils.RestaurantAdapter
import com.example.smartorder_app.utils.RestaurantData
import com.example.smartorder_app.utils.WorkingDays
import org.json.JSONArray

class RestaurantesFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: RestaurantAdapter
    private val restaurants = mutableListOf<RestaurantData>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.restaurants_fragment, container, false)

        recycler = view.findViewById(R.id.suggestions_recycler)
        recycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        adapter = RestaurantAdapter(restaurants) { restaurant ->
            Toast.makeText(requireContext(), "${restaurant.name} favorito!", Toast.LENGTH_SHORT).show()
        }

        recycler.adapter = adapter

        // Llamada a la API
        getAllRestaurants() { success, response ->
            if (success && response != null) {
                try {
                    val jsonArray = JSONArray(response)
                    val list = mutableListOf<RestaurantData>()

                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)
                        val workingDays = mutableListOf<WorkingDays>()
                        val wdArray = obj.optJSONArray("WorkingDays") ?: JSONArray()
                        for (j in 0 until wdArray.length()) {
                            val wdObj = wdArray.getJSONObject(j)
                            workingDays.add(
                                WorkingDays(
                                    wdObj.getString("day"),
                                    wdObj.getString("start"),
                                    wdObj.getString("end")
                                )
                            )
                        }
                        val contactObj = obj.getJSONObject("contact")
                        val phoneObj = contactObj.getJSONObject("phone")
                        val contact = ContactRestaurant(
                            email = contactObj.getString("email"),
                            countryCode = phoneObj.getString("countryCode"),
                            number = phoneObj.getString("number")
                        )

                        val restaurant = RestaurantData(
                            id = obj.getString("id"),
                            name = obj.getString("name"),
                            description = obj.optString("description", ""),
                            owner = obj.optString("owner", ""),
                            contact = contact,
                            imageUrl = obj.optString("imageUrl", ""),
                            logoUrl = obj.optString("logoUrl", ""),
                            images = listOf(), // puedes parsear si tu backend lo manda
                            WorkingDays = workingDays,
                            rating = obj.optDouble("rating", 0.0).toFloat(),
                            isFavorite = obj.optBoolean("isFavorite", false),
                            category = obj.optJSONArray("category")?.let { catArr ->
                                List(catArr.length()) { k -> catArr.getString(k) }
                            } ?: listOf(),
                            maxCapacity = obj.optInt("maxCapacity", 0),
                            currentCapacity = obj.optInt("currentCapacity", 0),
                            state = obj.optString("state", "available"),
                            status = obj.optString("status", "active"),
                            foods = obj.optJSONArray("foods")?.let { foodArr ->
                                List(foodArr.length()) { k -> foodArr.getString(k) }
                            } ?: listOf()
                        )

                        list.add(restaurant)
                    }

                    // Actualizar adapter
                    requireActivity().runOnUiThread {
                        restaurants.clear()
                        restaurants.addAll(list)
                        adapter.notifyDataSetChanged()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        return view
    }
}
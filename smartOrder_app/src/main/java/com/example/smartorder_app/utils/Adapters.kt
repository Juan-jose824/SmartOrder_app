package com.example.smartorder_app.utils

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartorder_app.Food
import com.example.smartorder_app.MainActivity
import com.example.smartorder_app.R
import com.example.smartorder_app.Restaurant

class RestaurantAdapter(
    private val restaurants: List<RestaurantData>,
    private val onFavoriteClick: (RestaurantData) -> Unit
) : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    inner class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.restaurant_image)
        private val logo: ImageView = itemView.findViewById(R.id.restaurant_logo)
        private val name: TextView = itemView.findViewById(R.id.restaurant_name)
        private val favIcon: ImageView = itemView.findViewById(R.id.fav_icon)
        private val open: TextView = itemView.findViewById(R.id.restaurant_open)
        private val close: TextView = itemView.findViewById(R.id.restaurant_close)
        private val rating: TextView = itemView.findViewById(R.id.rating)
        private val workTime: TextView = itemView.findViewById(R.id.work_time)

        fun bind(restaurant: RestaurantData) {
            name.text = restaurant.name
            val today = java.time.LocalDate.now().dayOfWeek.name.lowercase()
            val todayWorkTime = restaurant.WorkingDays.firstOrNull { it.day.lowercase() == today }
            if (todayWorkTime != null) {
                open.text = todayWorkTime.open
                close.text = todayWorkTime.close
            } else {
                open.text = "-"
                close.text = "-"
            }
            open.text = todayWorkTime?.open ?: "N/A"
            close.text = todayWorkTime?.close ?: "N/A"

            rating.text = restaurant.rating.toString()
            workTime.text = if (restaurant.status == "active") "Abierto" else "Cerrado"

            // Imagen principal y logo (usa Glide o Picasso)
            Glide.with(itemView.context)
                .load(restaurant.imageUrl)
                .placeholder(R.drawable.no_image)
                .into(image)

            Glide.with(itemView.context)
                .load(restaurant.logoUrl)
                .placeholder(R.drawable.no_restaurant_image)
                .into(logo)

            // Corazón de favorito
            favIcon.setImageResource(
                if (restaurant.isFavorite) R.drawable.heart else R.drawable.favorite
            )

            favIcon.setOnClickListener { onFavoriteClick(restaurant) }

            name.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, Restaurant::class.java)
                intent.putExtra("restaurant_name", restaurant.name) // ejemplo: pasar el nombre
                intent.putExtra("restaurant_id", restaurant.id)     // si tienes un id
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_restaurant, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(restaurants[position])
    }

    override fun getItemCount(): Int = restaurants.size
}

class FoodAdapter(
    private val foodList: List<FoodData>,
    private val onItemClick: (FoodData) -> Unit
) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodName: TextView = itemView.findViewById(R.id.foodName)
        val foodPrice: TextView = itemView.findViewById(R.id.foodPrice)
        //val foodRating: TextView = itemView.findViewById(R.id.foodRating)
        val foodDescription: TextView = itemView.findViewById(R.id.foodDescription)
        val foodImage: ImageView = itemView.findViewById(R.id.foodImage)

        fun bind(food: FoodData) {
            foodName.text = food.name
            foodPrice.text = "$${food.price}"
            //foodRating.text = " • 👍 ${food.sales}%"
            foodDescription.text = food.description

            // Carga la primera imagen de la lista (puedes mejorar con un carrusel si hay varias)
            if (food.images.isNotEmpty()) {
                Glide.with(itemView.context)
                    .load(food.images[0])
                    .placeholder(R.drawable.no_image) // imagen por defecto
                    .into(foodImage)
            }

            // Click en toda la card
            itemView.setOnClickListener {
                onItemClick(food)
                val context = itemView.context
                val intent = Intent(context, Food::class.java)
                intent.putExtra("food_name", food.name) // ejemplo: pasar el nombre
                intent.putExtra("food_restaurant", food.restaurant)     // restaurante del alimento
                intent.putExtra("food_price", food.price)     // precio del alimento
                intent.putExtra("food_description", food.description)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(foodList[position])
    }

    override fun getItemCount(): Int = foodList.size
}
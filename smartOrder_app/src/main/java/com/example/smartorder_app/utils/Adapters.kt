package com.example.smartorder_app.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartorder_app.R

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

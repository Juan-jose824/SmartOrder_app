package com.example.smartorder_app.utils

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
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
import androidx.core.content.edit

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

            favIcon.setOnClickListener {
                restaurant.isFavorite = !restaurant.isFavorite
                favIcon.setImageResource(
                    if (restaurant.isFavorite) R.drawable.heart else R.drawable.favorite
                )
                onFavoriteClick(restaurant)
            }

            image.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, Restaurant::class.java)
                intent.putExtra("restaurant_name", restaurant.name) // ejemplo: pasar el nombre
                intent.putExtra("restaurant_id", restaurant.id)     // si tienes un id
                context.startActivity(intent)
            }

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
    //private val onItemClick: (FoodData) -> Unit
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
                //onItemClick(food)
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

class CartAdapter(
    private val cartList: MutableList<CartItem>,
    private val onQuantityChange: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodName: TextView = itemView.findViewById(R.id.foodName)
        val foodPrice: TextView = itemView.findViewById(R.id.foodPrice)
        val foodQuantity: TextView = itemView.findViewById(R.id.quantityText)
        val foodImage: ImageView = itemView.findViewById(R.id.foodImage)
        val btnIncrease: Button = itemView.findViewById(R.id.btnIncreaseCart)
        val btnDecrease: Button = itemView.findViewById(R.id.btnDecreaseCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartList[position]
        holder.foodName.text = item.food.name
        holder.foodPrice.text = "$${item.getTotalPrice()}"
        holder.foodQuantity.text = item.quantity.toString()

        Glide.with(holder.itemView.context)
            .load(item.food.images.firstOrNull() ?: R.drawable.no_image)
            .into(holder.foodImage)

        holder.btnIncrease.setOnClickListener {
            item.quantity++
            notifyItemChanged(position)
            onQuantityChange()
        }

        holder.btnDecrease.setOnClickListener {
            if (item.quantity > 1) {
                item.quantity--
                notifyItemChanged(position)
            } else {
                cartList.removeAt(position)
                notifyItemRemoved(position)
            }
            onQuantityChange()
        }
    }

    override fun getItemCount(): Int = cartList.size
}

class PlaceAdapter(
    private val places: List<PlaceData>,
    private val onItemChecked: (PlaceData, Boolean) -> Unit
) : RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeName: TextView = itemView.findViewById(R.id.placeName)
        val placeDescription: TextView = itemView.findViewById(R.id.placeDescription)
        val placeImage: ImageView = itemView.findViewById(R.id.placeImage)
        val placeCheckBox: CheckBox = itemView.findViewById(R.id.placeCheckBox)
        val placeState: TextView = itemView.findViewById(R.id.status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_place, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = places[position]

        holder.placeName.text = place.name
        holder.placeDescription.text = place.description
        holder.placeState.text = place.status

        // cargar primera imagen si existe
        if (place.images.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(place.images[0])
                .placeholder(R.drawable.no_image)
                .into(holder.placeImage)
        } else {
            holder.placeImage.setImageResource(R.drawable.no_image)
        }

        // checkbox listener
        holder.placeCheckBox.setOnCheckedChangeListener { _, isChecked ->
            onItemChecked(place, isChecked)
        }
    }

    override fun getItemCount() = places.size
}


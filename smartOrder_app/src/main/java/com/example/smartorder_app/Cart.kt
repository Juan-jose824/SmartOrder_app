package com.example.smartorder_app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartorder_app.utils.CartAdapter
import com.example.smartorder_app.utils.CartItem
import com.example.smartorder_app.utils.CartManager
import com.example.smartorder_app.utils.FoodAdapter

class Cart : AppCompatActivity() {

    private lateinit var recyclerCart: RecyclerView
    private lateinit var cartAdapter: CartAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.cart)

        recyclerCart = findViewById(R.id.recyclerCart)

        // Ejemplo: Recuperar carrito desde Singleton
        val cartItems = CartManager.getCart()

        cartAdapter = CartAdapter(cartItems) {
            updateTotal()
        }

        recyclerCart.layoutManager = LinearLayoutManager(this)
        recyclerCart.adapter = cartAdapter

        updateTotal()

        val subtotal: TextView = findViewById(R.id.subTotal)


        val btnNext: Button = findViewById(R.id.btnPay)

        btnNext.setOnClickListener {
            val intent = Intent(this, Reservation::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateTotal() {
        val total = CartManager.getCart().sumOf { it.getTotalPrice() }
        val subtotal: TextView = findViewById(R.id.subTotal)

        subtotal.text = "$${"%.2f".format(total)}"
    }
}
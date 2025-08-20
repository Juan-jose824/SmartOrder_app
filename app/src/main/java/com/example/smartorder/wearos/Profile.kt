package com.example.smartorder.wearos

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.smartorder.R

class Profile: ComponentActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContentView(R.layout.profile)

        val prefs = getSharedPreferences("smartorder_watch", Context.MODE_PRIVATE)

        findViewById<TextView>(R.id.tv_name).text = prefs.getString("name", "Sin nombre")
        findViewById<TextView>(R.id.tv_email).text = prefs.getString("email", "Sin email")
        findViewById<TextView>(R.id.tv_cellphone).text = prefs.getString("cellphone", "Sin teléfono")
        findViewById<TextView>(R.id.tv_role).text = "Rol: " + prefs.getString("role", "N/A")
        findViewById<TextView>(R.id.tv_date).text = prefs.getString("date", "Fecha desconocida")
    }
}
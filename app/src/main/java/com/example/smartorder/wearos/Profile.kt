package com.example.smartorder.wearos

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.smartorder.R
import com.example.smartorder.wearos.utils.PrefsManager

class Profile: ComponentActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContentView(R.layout.profile)

        val prefs = PrefsManager.loadUser(this)

        Log.i("PREFS", prefs.toString())

        val fullName = "${prefs?.name?.name} ${prefs?.name?.fatherName} ${prefs?.name?.motherName}"
        val email = prefs?.email ?: "No disponiblel"
        val cellphone = "${prefs?.cellphone?.countryCode} ${prefs?.cellphone?.number}"
        val role = prefs?.role ?: "No disponible"
        val date = "No disponible"

        // Asignar valores a los TextViews
        findViewById<TextView>(R.id.tv_name).text = fullName.toString()
        findViewById<TextView>(R.id.tv_email).text = email
        findViewById<TextView>(R.id.tv_cellphone).text = cellphone
        findViewById<TextView>(R.id.tv_role).text = "Rol: ${roleToString(role)}"
        findViewById<TextView>(R.id.tv_date).text = date
    }

    private fun roleToString(role: String?): String {
        return when(role) {
            "1" -> "Usuario"
            "2" -> "Administrador"
            else -> "Desconocido"
        }
    }
}
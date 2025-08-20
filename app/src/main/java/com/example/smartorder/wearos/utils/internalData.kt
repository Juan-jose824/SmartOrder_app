package com.example.smartorder.wearos.utils

import android.content.Context
import androidx.core.content.edit

object PrefsManager {
    private const val PREFS_NAME = "smartorder_prefs"

    fun saveUser(context: Context, user: UserConfig) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
            putString("userEmail", user.email)
            putString("userName", user.name.name)
            putString("userMiddleName", user.name.fatherName)
            putString("userLastName", user.name.motherName)
            putString("userCellphone", user.cellphone.number)
            putBoolean("isLoggedIn", true)
        }
    }

    fun loadUser(context: Context): UserConfig? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val email = prefs.getString("userEmail", null)
        if (email == null) return null
        return UserConfig(
            Name(
                prefs.getString("userName", "") ?: "",
                "",
                ""
            ),
            email,
            Cellphone("", prefs.getString("userCellphone", "") ?: ""),
            "1",
            "12345",
            prefs.getBoolean("isLoggedIn", false)
        )
    }

    fun saveConfig(context: Context, config: AppConfig) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
            putString("user", config.user)
            putString("theme", config.theme)
            putBoolean("reservations", config.reservations)
            putBoolean("publicWorkTime", config.publicWorkTime)
            putBoolean("publicDescription", config.publicDescription)
            putBoolean("notifications", config.notifications)
            putBoolean("alerts", config.alerts)
            putBoolean("onlyRestaurant", config.onlyRestaurant)
            putBoolean("reports", config.reports)
            putBoolean("grafics", config.grafics)
            putBoolean("adminUsers", config.adminUsers)
            putBoolean("adminFoods", config.adminFoods)
            putBoolean("adminPlaces", config.adminPlaces)
            putBoolean("adminReservations", config.adminReservations)
        }
    }

    fun loadConfig(context: Context): AppConfig {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return AppConfig(
            user = prefs.getString("user", "") ?: "",
            theme = prefs.getString("theme", "Dark") ?: "Dark",
            reservations = prefs.getBoolean("reservations", true),
            publicWorkTime = prefs.getBoolean("publicWorkTime", true),
            publicDescription = prefs.getBoolean("publicDescription", true),
            notifications = prefs.getBoolean("notifications", true),
            alerts = prefs.getBoolean("alerts", true),
            onlyRestaurant = prefs.getBoolean("onlyRestaurant", false),
            reports = prefs.getBoolean("reports", false),
            grafics = prefs.getBoolean("grafics", false),
            adminUsers = prefs.getBoolean("adminUsers", false),
            adminFoods = prefs.getBoolean("adminFoods", false),
            adminPlaces = prefs.getBoolean("adminPlaces", false),
            adminReservations = prefs.getBoolean("adminReservations", false),
        )
    }

    fun clear(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit { clear() }
    }
}
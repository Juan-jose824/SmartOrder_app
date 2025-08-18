package utils

import android.content.Context
import com.example.smartorder_app.utils.UserConfig
import com.google.gson.Gson

class InternalData(private val context: Context) {
    private val gson = Gson()
    private val fileName = "config.json"
    private val cartFile = "cart.json"

    fun save(config: UserConfig) {
        val jsonString = gson.toJson(config)
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
        }
    }

    fun getData(): UserConfig? {
        return try {
            val json = context.openFileInput(fileName).bufferedReader().use { it.readText() }
            gson.fromJson(json, UserConfig::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
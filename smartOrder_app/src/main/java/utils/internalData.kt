package utils

import android.content.Context
import com.google.gson.Gson

data class UserConfig(
    val name: Name,
    val email: String,
    val cellphone: Cellphone,
    val role: String,
    val token: String,
    var isLoggedIn: Boolean = false
)

data class Name(
    val name: String,
    val fatherName: String,
    val motherName: String
)

data class Cellphone(
    val countryCode: String,
    val number: String
)

class InternalData(private val context: Context) {
    private val gson = Gson()
    private val fileName = "config.json"

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
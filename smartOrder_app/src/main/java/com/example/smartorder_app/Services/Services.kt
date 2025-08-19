package com.example.smartorder_app.Services

import android.util.Log
import com.example.smartorder_app.utils.Cellphone
import com.example.smartorder_app.utils.Name
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

private val client = OkHttpClient()

private val BACK_URI = "https://csb2wwrf-5102.usw3.devtunnels.ms/api/"

fun singIn(
    name: Name,
    cellphone: Cellphone,
    email: String,
    password: String,
    role: Int,
    onResult: (Boolean, String?) -> Unit // callback con resultado y mensaje
) {
    val nameJson = JSONObject().apply {
        put("name", name.name)
        put("paternal_surname", name.fatherName)
        put("maternal_surname", name.motherName)
    }

    val json = JSONObject().apply {
        put("name", nameJson)
        put("cellphone", cellphone.number)
        put("email", email)
        put("password", password)
        put("role", role)
    }

    //name, cellphone, password, email, role
    val request = Request.Builder()
        .url(BACK_URI + "singIn")
        .post(RequestBody.create("application/json".toMediaType(), json.toString()))
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("HTTP", "Error: ${e.message}")
            onResult(false, null) // falla
        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string()
            if (response.isSuccessful && responseBody != null) {
                try {
                    val jsonResponse = JSONObject(responseBody)
                    val data = jsonResponse.optString("data")
                    //Log.d("Resultado", data.toString())

                    onResult(true, data)
                } catch (e: Exception) {
                    //Log.e("error", response.toString())
                    onResult(false, null)
                }
            } else {
                val jsonResponse = JSONObject(responseBody)
                val data = jsonResponse.optString("data")
                //Log.e("Resultado error", data.toString())

                onResult(false, data) // error en la respuesta
            }
        }
    })
}

fun login(
    email: String,
    password: String,
    remember: Boolean,
    onResult: (Boolean, String?) -> Unit // callback con resultado y mensaje
) {
    val json = JSONObject()
    json.put("email", email)
    json.put("password", password)
    json.put("remember", remember)

    val request = Request.Builder()
        .url(BACK_URI + "login")
        .post(RequestBody.create("application/json".toMediaType(), json.toString()))
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("HTTP", "Error: ${e.message}")
            onResult(false, null) // falla
        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string()
            if (response.isSuccessful && responseBody != null) {
                try {
                    val jsonResponse = JSONObject(responseBody)
                    val data = jsonResponse.optString("data")
                    //Log.d("Resultado", data.toString())

                    onResult(response.isSuccessful, responseBody)
                } catch (e: Exception) {
                    Log.e("error", response.toString())
                    onResult(false, null)
                }
            } else {
                val jsonResponse = JSONObject(responseBody)
                val data = jsonResponse.optString("data")
                //Log.e("Resultado error", data.toString())

                onResult(false, data) // error en la respuesta
            }
        }
    })
}

fun getAllRestaurants(
    onResult: (Boolean, String?) -> Unit
) {
    val url = BACK_URI + "restaurants?skip=0&limit=1000"

    val request = Request.Builder()
        .url(url)
        .get()
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("HTTP", "Error: ${e.message}")
            onResult(false, null)
        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string()
            if (response.isSuccessful && responseBody != null) {
                try {
                    val jsonResponse = JSONObject(responseBody)
                    val data = jsonResponse.optJSONArray("data")
                    onResult(true, data.toString())
                } catch (e: Exception) {
                    onResult(false, null)
                }
            } else {
                val data = responseBody?.let { JSONObject(it).optString("data") }
                onResult(false, data)
            }
        }
    })
}

fun getRestaurant(
    restaurant: String,
    onResult: (Boolean, String?) -> Unit
) {
    val url = "${BACK_URI}restaurants/${restaurant}"

    val request = Request.Builder()
        .url(url)
        .get()
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("HTTP", "Error: ${e.message}")
            onResult(false, null)
        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string()
            if (response.isSuccessful && responseBody != null) {
                try {
                    val jsonResponse = JSONObject(responseBody)

                    onResult(true, responseBody)
                } catch (e: Exception) {
                    onResult(false, null)
                }
            } else {
                val data = responseBody?.let { JSONObject(it).optString("data") }
                onResult(false, data)
            }
        }
    })
}

fun getRestaurantFoods(
    restaurant: String,
    onResult: (Boolean, String?) -> Unit
) {
    val url = "${BACK_URI}restaurants/${restaurant}/foods"

    val request = Request.Builder()
        .url(url)
        .get()
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("HTTP", "Error: ${e.message}")
            onResult(false, null)
        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string()
            if (response.isSuccessful && responseBody != null) {
                try {
                    val jsonResponse = JSONObject(responseBody)
                    val data = jsonResponse.optString("data")
                    onResult(true, data)
                } catch (e: Exception) {
                    onResult(false, null)
                }
            } else {
                val data = responseBody?.let { JSONObject(it).optString("data") }
                onResult(false, data)
            }
        }
    })
}

fun getFood(
    restaurant: String,
    food: String,
    onResult: (Boolean, String?) -> Unit
) {
    val url = "${BACK_URI}restaurants/${restaurant}/foods/${food}"

    val request = Request.Builder()
        .url(url)
        .get()
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("HTTP", "Error: ${e.message}")
            onResult(false, null)
        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string()
            if (response.isSuccessful && responseBody != null) {
                try {
                    val jsonResponse = JSONObject(responseBody)
                    val data = jsonResponse.optString("data")
                    onResult(true, data)
                } catch (e: Exception) {
                    onResult(false, null)
                }
            } else {
                val data = responseBody?.let { JSONObject(it).optString("data") }
                onResult(false, data)
            }
        }
    })
}
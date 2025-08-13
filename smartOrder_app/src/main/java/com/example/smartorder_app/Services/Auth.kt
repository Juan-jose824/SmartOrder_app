package com.example.smartorder_app.Services

import android.util.Log
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

val BACK_URI = "https://csb2wwrf-5102.usw3.devtunnels.ms/"

fun login(
    user: String,
    email: String,
    onResult: (Boolean, String?) -> Unit // callback con resultado y mensaje
) {
    val json = JSONObject()
    json.put("user", user)
    json.put("email", email)

    val request = Request.Builder()
        .url(BACK_URI + "newUser")
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
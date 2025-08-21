package com.example.smartorder_app.utils

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataMap
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable

fun sendUserToWatch(context: Context, user: UserConfig) {
    val dataClient: DataClient = Wearable.getDataClient(context)

    val putDataMapReq = PutDataMapRequest.create("/userData").apply {
        dataMap.putString("email", user.email)
        dataMap.putString("name", user.name.name)
        dataMap.putString("paternal", user.name.fatherName)
        dataMap.putString("maternal", user.name.motherName)
        dataMap.putString("cellphone", user.cellphone.number)
        dataMap.putString("role", user.role)
        dataMap.putBoolean("isLogged", user.isLoggedIn)
    }

    val request = putDataMapReq.asPutDataRequest().setUrgent()

    dataClient.putDataItem(request)
        .addOnSuccessListener {
            Log.d("Phone", "DataItem enviado correctamente")
        }
        .addOnFailureListener {
            Log.e("Phone", "Error al enviar DataItem", it)
        }
}

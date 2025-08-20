package com.example.smartorder.wearos.utils

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.WearableListenerService

class DataLayerListenerService : WearableListenerService() {

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val path = event.dataItem.uri.path
                if (path == "/user_profile") {
                    val dataMap = com.google.android.gms.wearable.DataMapItem.fromDataItem(event.dataItem).dataMap

                    val name = dataMap.getString("name", "")
                    val paternal = dataMap.getString("paternal", "")
                    val maternal = dataMap.getString("maternal", "")
                    val email = dataMap.getString("email", "")
                    val cellphone = dataMap.getString("cellphone", "")
                    val role = dataMap.getString("role", "")
                    val date = dataMap.getString("date", "")
                    val time = dataMap.getString("time", "")

                    // Guardar en SharedPreferences del reloj para luego mostrar
                    val prefs = getSharedPreferences("smartorder_watch", Context.MODE_PRIVATE)
                    prefs.edit().apply {
                        putString("name", "$name $paternal $maternal")
                        putString("email", email)
                        putString("cellphone", cellphone)
                        putString("role", role)
                        putString("date", "$date $time")
                        apply()
                    }

                    Log.d("WearOS", "Usuario recibido: $name $paternal $maternal")
                }
            }
        }
    }
}

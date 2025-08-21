package com.example.smartorder.wearos.utils

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import org.json.JSONObject

class DataLayerListenerService : WearableListenerService() {

    override fun onMessageReceived(messageEvent: MessageEvent) {
        Log.d("WearOS", "Mensaje recibido: ${messageEvent.path}")
        val data = String(messageEvent.data)
        Log.d("WearOS", "Contenido: $data")
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val path = event.dataItem.uri.path
                if (path == "/userData") {
                    Log.d("WearOS", "onDataChanged disparado con path: $path")

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

fun sendMessageToPhone(path: String, data: dataSensors, context: Context) {
    val nodeClient = Wearable.getNodeClient(context)
    val messageClient = Wearable.getMessageClient(context)

    try {
        val json = JSONObject().apply {
            put("bpm", data.bpm)
            put("pressure", data.pressure)
            put("gyroscope", JSONObject().apply {
                put("x", data.gyroscope.x)
                put("y", data.gyroscope.y)
                put("z", data.gyroscope.z)
            })
        }

        nodeClient.connectedNodes.addOnSuccessListener { nodes ->
            nodes.forEach { node ->
                messageClient.sendMessage(node.id, path, json.toString().toByteArray())
                    .addOnSuccessListener {
                        Log.d("Wear", "Mensaje enviado al teléfono con éxito a ${node.displayName}")
                    }
                    .addOnFailureListener {
                        Log.e("Wear", "Error al enviar mensaje al teléfono", it)
                    }
            }
        }
    } catch (e: Exception) {
        Log.e("Wear", "Error creando JSON o enviando mensaje", e)
    }
}
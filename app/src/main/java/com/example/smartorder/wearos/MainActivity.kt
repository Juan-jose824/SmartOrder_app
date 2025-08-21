/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.example.smartorder.wearos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.smartorder.R
import com.example.smartorder.wearos.theme.SmartOrderTheme
import com.example.smartorder.wearos.utils.Cellphone
import com.example.smartorder.wearos.utils.Name
import com.example.smartorder.wearos.utils.PrefsManager
import com.example.smartorder.wearos.utils.UserConfig
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataMapItem


class MainActivity : ComponentActivity(), MessageClient.OnMessageReceivedListener, DataClient.OnDataChangedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)

        // Registrar listeners en tiempo de ejecución
        Wearable.getMessageClient(this).addListener(this)
        Wearable.getDataClient(this).addListener(this)

        val profile : ImageButton = findViewById(R.id.btn_perfil)
        val userState: ImageButton = findViewById(R.id.btnUserState)

        profile.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
        }

        userState.setOnClickListener {
            startActivity(Intent(this, UserState::class.java))
        }
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        Log.d("Wear", "Mensaje recibido: ${messageEvent.path}")

        if (messageEvent.path == "/path/message") {
            val msg = String(messageEvent.data)
            runOnUiThread {
                Toast.makeText(this, "Recibido: $msg", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        for (event in dataEvents) {
            when (event.type) {
                DataEvent.TYPE_CHANGED -> {
                    Log.d("Wear", "DataItem cambiado: ${event.dataItem.uri}")
                }
                DataEvent.TYPE_DELETED -> {
                    Log.d("Wear", "DataItem eliminado: ${event.dataItem.uri}")
                }
            }
            if (event.type == DataEvent.TYPE_CHANGED) {
                val path = event.dataItem.uri.path
                if (path == "/userData") {
                    val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                    val name = dataMap.getString("name") ?: "user"
                    val paternal = dataMap.getString("paternal") ?: ""
                    val maternal = dataMap.getString("maternal") ?: ""
                    val cellphoneStr = dataMap.getString("cellphone") ?: ""
                    val email = dataMap.getString("email") ?: ""
                    val token = dataMap.getString("token") ?: ""
                    val role = dataMap.getString("role") ?: ""
                    val isLoggedIn = dataMap.getBoolean("isLogged", false)

                    Log.d("WearOS", "Perfil recibido: $name $paternal $maternal - $email")

                    if (email.isNotEmpty()) {
                        val user = UserConfig(
                            name = Name(name, paternal, maternal),
                            email = email,
                            cellphone = Cellphone("+52", cellphoneStr),
                            role = role,
                            token = token,
                            isLoggedIn = isLoggedIn
                        )

                        Log.i("Datos", user.toString())
                        PrefsManager.saveUser(this, user)


                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Wearable.getMessageClient(this).removeListener(this)
        Wearable.getDataClient(this).removeListener(this)
    }
}

@Composable
fun WearApp(greetingName: String) {
    SmartOrderTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            TimeText()
            Greeting(greetingName = greetingName)
        }
    }
}

@Composable
fun Greeting(greetingName: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = stringResource(R.string.hello_world, greetingName)
    )
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
}
package com.example.smartorder_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartorder_app.ui.theme.SmartOrderTheme
import utils.Cellphone
import utils.InternalData
import utils.Name
import utils.UserConfig

class MainActivity : ComponentActivity() {
    private lateinit var config: UserConfig
    private lateinit var localStorage: InternalData
    private val interval = 30_000L

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        localStorage = InternalData(this)
        config = localStorage.getData() ?:
        UserConfig(Name("Usuario", "", ""), "", Cellphone("", ""), "0", "", false)

        if (config.isLoggedIn == true) {
            setContentView(R.layout.main)
        } else {
            setContentView(R.layout.login)

            val singingButton : Button =findViewById(R.id.singinButton)

            singingButton.setOnClickListener {

            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SmartOrderTheme {
        Greeting("Android")
    }
}
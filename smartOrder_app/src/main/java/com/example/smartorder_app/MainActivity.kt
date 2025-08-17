package com.example.smartorder_app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartorder_app.ui.theme.SmartOrderTheme
import com.google.android.material.bottomnavigation.BottomNavigationView
import utils.InternalData
import androidx.fragment.app.Fragment
import com.example.smartorder_app.utils.Cellphone
import com.example.smartorder_app.utils.Name
import com.example.smartorder_app.utils.UserConfig

class MainActivity : AppCompatActivity() {
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

            val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

            replaceFragment(RestaurantesFragment())

            bottomNav.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_restaurantes -> {
                        replaceFragment(RestaurantesFragment())
                        true
                    }
                    R.id.nav_opciones -> {
                        // TODO: Acción para Opciones
                        true
                    }
                    R.id.nav_configuracion -> {
                        // TODO: Acción para Configuración
                        true
                    }
                    R.id.nav_perfil -> {
                        replaceFragment(ProfileFragment())
                        true
                    }
                    else -> false
                }
            }

        } else {
            setContentView(R.layout.login)

            val login : Button = findViewById(R.id.loginButton)
            val singingButton : Button =findViewById(R.id.singinbutton)

            var emailInput: EditText = findViewById(R.id.email)
            var passwordInput: EditText = findViewById(R.id.password)

            login.setOnClickListener {
                val email = emailInput.text.toString().trim()
                val password = passwordInput.text.toString().trim()

                if ((email == "") || password == "") {
                    Toast.makeText(
                        this@MainActivity,
                        "Por favor ingrese los datos requeridos",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val newConfig = UserConfig(
                        Name("Francisco", "", ""),
                        email,
                        Cellphone("", ""),
                        "1",
                        "12345",
                     true
                    )
                    localStorage.save(newConfig)
                    Log.i("Dstos:", "$email $password")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }

            singingButton.setOnClickListener {
                val intent = Intent(this, SinIn::class.java)
                startActivity(intent)
            }
        }
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
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
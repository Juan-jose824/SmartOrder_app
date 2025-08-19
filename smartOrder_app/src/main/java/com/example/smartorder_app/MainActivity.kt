package com.example.smartorder_app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartorder_app.ui.theme.SmartOrderTheme
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import com.auth0.jwt.JWT
import com.example.smartorder_app.Services.login
import com.example.smartorder_app.utils.Cellphone
import com.example.smartorder_app.utils.Name
import com.example.smartorder_app.utils.PrefsManager
import com.example.smartorder_app.utils.UserConfig
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Comprobar si el usuario ya está loggeado
        val user = PrefsManager.loadUser(this)
        if (user == null || !user.isLoggedIn) {
            // Mostrar login si no hay usuario o no está loggeado
            setContentView(R.layout.login)
            setupLogin()
        } else {
            // Usuario loggeado: mostrar main
            setContentView(R.layout.main)
            setupMain()
        }
    }

    private fun setupLogin() {
        val login: Button = findViewById(R.id.loginButton)
        val signingButton: Button = findViewById(R.id.singinbutton)
        val emailInput: EditText = findViewById(R.id.email)
        val passwordInput: EditText = findViewById(R.id.password)
        val rememberMe: CheckBox = findViewById(R.id.rememeberme) // si tienes un switch para "recordarme"

        login.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val remember = rememberMe.isChecked

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor ingrese los datos requeridos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Llamar a la función login que hace la petición HTTP
            login(email, password, remember) { success, responseBody ->
                runOnUiThread {
                    if (success && responseBody != null) {
                        try {
                            val jsonResp = JSONObject(responseBody)

                            val nameObj = jsonResp.optJSONObject("user_name")
                            val cellphoneObj = jsonResp.optJSONObject("user_cellphone")

                            val newUser = UserConfig(
                                Name(
                                    nameObj?.optString("name") ?: "",
                                    nameObj?.optString("paternal_surname") ?: "",
                                    nameObj?.optString("maternal_surname") ?: ""
                                ),
                                jsonResp.optString("user_email"),
                                Cellphone(
                                    cellphoneObj?.optString("countryCode") ?: "",
                                    cellphoneObj?.optString("number") ?: ""
                                ),
                                jsonResp.optInt("user_role").toString(),
                                jsonResp.optString("token"),
                                remember
                            )

                            PrefsManager.saveUser(this, newUser)

                            // Abrir MainActivity
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(this, "Error al procesar los datos del usuario", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, responseBody ?: "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        signingButton.setOnClickListener {
            startActivity(Intent(this, SinIn::class.java))
        }
    }


    private fun setupMain() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Cargar fragment por defecto
        replaceFragment(RestaurantesFragment())

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_restaurantes -> replaceFragment(RestaurantesFragment())
                R.id.nav_opciones -> replaceFragment(History_Fragment())
                R.id.nav_configuracion -> replaceFragment(Settings_Fragment())
                R.id.nav_perfil -> replaceFragment(ProfileFragment())
                else -> false
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
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
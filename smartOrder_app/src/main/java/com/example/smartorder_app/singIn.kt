package com.example.smartorder_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smartorder_app.Services.singIn
import com.example.smartorder_app.utils.Cellphone
import com.example.smartorder_app.utils.Name

class SinIn : ComponentActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContentView(R.layout.sing_in)

        val singIn: Button = findViewById(R.id.singinbutton)
        val scrollView = findViewById<ScrollView>(R.id.formScroll)

        var nameInput: EditText = findViewById(R.id.name)
        var fathernameInput: EditText = findViewById(R.id.fatherName)
        var mothernameInput: EditText = findViewById(R.id.motherName)
        var emailInput: EditText = findViewById(R.id.email)
        var cellphoneInput: EditText = findViewById(R.id.cellphone)
        var passwordInput: EditText = findViewById(R.id.password)

        ViewCompat.setOnApplyWindowInsetsListener(scrollView) { view, insets ->
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            // aplica el bottom inset como padding para que el scroll muestre el campo
            view.setPadding(
                view.paddingLeft,
                view.paddingTop,
                view.paddingRight,
                imeInsets.bottom
            )
            insets
        }
        // solicitar que se apliquen insets inicialmente
        ViewCompat.requestApplyInsets(scrollView)

        singIn.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val fatherName = fathernameInput.text.toString().trim()
            val motherName = mothernameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val cellphone = cellphoneInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val role = 5

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Complete los campos requeridos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nameObj = Name(name, fatherName, motherName)
            val cellObj = Cellphone("+52", cellphone)

            singIn(nameObj, cellObj, email, password, role) { success, message ->
                runOnUiThread {
                    if (success) {
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        finish() // cerrar actividad y volver al login
                    } else {
                        Toast.makeText(this, "Error: ${message ?: "Intente nuevamente"}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
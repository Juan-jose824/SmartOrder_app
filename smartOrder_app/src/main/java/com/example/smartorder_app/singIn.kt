package com.example.smartorder_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
            var name = nameInput.text.toString().trim()
            var fathername = fathernameInput.text.toString().trim()
            var mothername = mothernameInput.text.toString().trim()
            var email = emailInput.text.toString().trim()
            var cellphone = cellphoneInput.text.toString().trim()
            var password = passwordInput.text.toString().trim()
            val rol = 5

            Log.i("Registro", "$name $fathername $mothername $email")
        }
    }
}
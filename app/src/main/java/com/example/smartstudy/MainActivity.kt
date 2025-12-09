package com.example.smartstudy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DBHelper(this)
        var userName = findViewById<EditText>(R.id.usuarioIng)
        var password = findViewById<EditText>(R.id.contraseñaIng)
        var btnIngresar = findViewById<Button>(R.id.ingresar_i)
        var btnRegistrase = findViewById<Button>(R.id.registrarse_i)
        var alertaIng = findViewById<TextView>(R.id.alertaIng)

        btnRegistrase.setOnClickListener {
            val intent = Intent(this, Registrarse::class.java)
            startActivity(intent)
        }

        btnIngresar.setOnClickListener {
            val user = userName.text.toString().trim()
            val pass = password.text.toString().trim()

            if (user.isNotEmpty() && pass.isNotEmpty()) {
                login(user, pass)
            } else {
                alertaIng.text = "Por favor, completa todos los campos"
                alertaIng.visibility = View.VISIBLE

            }
        }
    }

    //funcion iniciar sesion
    private  fun login(username: String, pass: String){
        val usuario = databaseHelper.leerUsuario(username, pass)
        if(usuario){
            Toast.makeText(this, "Iniciaste sesion", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Agenda::class.java)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
    }
}
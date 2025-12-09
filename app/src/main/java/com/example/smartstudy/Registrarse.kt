package com.example.smartstudy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class Registrarse : AppCompatActivity() {
    private lateinit var databaseHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrarse)

        databaseHelper = DBHelper(this)

        var nombreReg = findViewById<EditText>(R.id.nombreReg)
        var correoReg = findViewById<EditText>(R.id.correoReg)
        var usuarioReg = findViewById<EditText>(R.id.usuarioReg)
        var contrasenaReg = findViewById<EditText>(R.id.contraseñaReg)
        var btnRegistrarseReg = findViewById<Button>(R.id.crearTarea)
        var btnIngresarReg = findViewById<Button>(R.id.ingresarReg)
        var alerta = findViewById<TextView>(R.id.alertaReg)

        btnIngresarReg.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        btnRegistrarseReg.setOnClickListener {
            val nombre = nombreReg.text.toString().trim()
            val correo = correoReg.text.toString().trim()
            val user = usuarioReg.text.toString().trim()
            val pass = contrasenaReg.text.toString().trim()

            if (nombre.isNotEmpty() && correo.isNotEmpty() && user.isNotEmpty() && pass.isNotEmpty()) {
                if (isValidEmail(correo)) {
                    alerta.visibility = View.GONE
                    registrarUser(nombre, correo, user, pass)
                } else {
                    alerta.text = "Por favor, verifica el formato del correo"
                    alerta.visibility = View.VISIBLE
                }
            } else {
                alerta.text = "Por favor, completa todos los campos"
                alerta.visibility = View.VISIBLE
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Mandar datos para registar usuario
    private fun registrarUser(nombre: String, correo: String, user: String, pass: String){
        val idregistro = databaseHelper.insertarUsuario(nombre, correo, user, pass)
        if(idregistro != -1L){
            Toast.makeText(this, "Registro Exitoso", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(this, "Error en el Registro", Toast.LENGTH_SHORT).show()
        }
    }
}
package com.example.smartstudy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout

class EditarPerfil : AppCompatActivity() {

    private lateinit var databaseHelper: DBHelper
    private lateinit var nombreEdi: TextInputLayout
    private lateinit var correoEdi: TextInputLayout
    private lateinit var usuarioEdi: TextInputLayout
    private lateinit var contrasenaEdi: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        databaseHelper = DBHelper(this)

        val btnRegrasar = findViewById<ImageView>(R.id.regresarPer)
        btnRegrasar.setOnClickListener {
            val intent = Intent(this, Perfil::class.java)
            startActivity(intent)
        }

        val ID = intent.getStringExtra("UserID")
        nombreEdi = findViewById(R.id.nombreEdiPer)
        correoEdi = findViewById(R.id.correoEdiPer)
        usuarioEdi = findViewById(R.id.usuarioEdiPer)
        contrasenaEdi = findViewById(R.id.contraseñaEdiPer)
        val btnEditarPer = findViewById<Button>(R.id.btnEditarPerfil)

        // Convierte el ID a Int de forma segura
        val id = ID?.toIntOrNull()

        if (id != null) {
            leerUsuarios(id)

            btnEditarPer.setOnClickListener {
                // Usa el ID directamente, ya que ya es un Int
                val nombre = nombreEdi.editText?.text.toString()
                val correo = correoEdi.editText?.text.toString()
                val usuario = usuarioEdi.editText?.text.toString()
                val contrasena = contrasenaEdi.editText?.text.toString()

                userUpdate(id, nombre, correo, usuario, contrasena)
            }
        } else {
            Toast.makeText(this, "Error en el ID recibido", Toast.LENGTH_SHORT).show()
            finish()
        }

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            finish() // Close the activity in case of an error
        }
    }

    private fun leerUsuarios(ID: Int) {
        val db = databaseHelper.leerActualizar(ID)
        if (db != null && db.moveToFirst()) {
            nombreEdi.editText?.setText(db.getString(1).toString())
            correoEdi.editText?.setText(db.getString(2).toString())
            usuarioEdi.editText?.setText(db.getString(3).toString())
            contrasenaEdi.editText?.setText(db.getString(4).toString())
            db.close()
        } else {
            Toast.makeText(this, "No se encontraron datos para el ID dado", Toast.LENGTH_SHORT).show()
        }
    }


    private fun userUpdate(id: Int, nombre: String, correo: String, usuario: String, contrasena: String) {
        val idRegistro = databaseHelper.actualizar(id, nombre, correo, usuario, contrasena)
        if (idRegistro != 0) {
            Toast.makeText(this, "Actualización Exitosa", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Perfil::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Error en la actualización", Toast.LENGTH_SHORT).show()
        }
    }
}
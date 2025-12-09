package com.example.smartstudy

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class Perfil : MenuLogic() {
    private lateinit var databaseHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val navigationView: NavigationView = findViewById(R.id.nav_view)

        setup(drawer, toolbar, navigationView)


        databaseHelper = DBHelper(this)

        val nombrePerfil = findViewById<TextView>(R.id.verNombrePerfil)
        leerNombre(nombrePerfil)
        val correoPerfil = findViewById<TextView>(R.id.verCorreoPerfil)
        leerCorreo(correoPerfil)
        val usuarioPerfil = findViewById<TextView>(R.id.verUsuarioPerfil)
        leerUSuario(usuarioPerfil)

        val idPerfil = findViewById<EditText>(R.id.idPerfil)
        leerid(idPerfil)

        var btnEditPerfil = findViewById<ImageView>(R.id.iconEditPerfil)
        btnEditPerfil.setOnClickListener {
            val ID = idPerfil.text.toString()
            if (ID.isNotEmpty()) {
                val intent = Intent(this, EditarPerfil::class.java)
                intent.putExtra("UserID", ID)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, ingrese un ID", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Imprimir el nombre
    private fun leerNombre(nombrePerfil: TextView) {
        val sql = "SELECT NOMBRE FROM USUARIOS"
        val db = databaseHelper.verPerfil(sql)

        if (db != null) {
            if (db.moveToFirst()) {
                do {
                    val nombre = db.getString(db.getColumnIndex("NOMBRE"))
                    nombrePerfil.append("$nombre")
                } while (db.moveToNext())
            }
        }
    }

    // Imprimir el Correo
    private fun leerCorreo(correoPerfil: TextView) {
        val sql = "SELECT CORREO FROM USUARIOS"
        val db = databaseHelper.verPerfil(sql)

        if (db != null) {
            if (db.moveToFirst()) {
                do {
                    val correo = db.getString(db.getColumnIndex("CORREO"))
                    correoPerfil.append("$correo")
                } while (db.moveToNext())
            }
        }
    }

    // Imprimir el Usuario
    private fun leerUSuario(usuarioPerfil: TextView) {
        val sql = "SELECT USUARIO FROM USUARIOS"
        val db = databaseHelper.verPerfil(sql)

        if (db != null) {
            if (db.moveToFirst()) {
                do {
                    val usuario = db.getString(db.getColumnIndex("USUARIO"))
                    usuarioPerfil.append("$usuario")
                } while (db.moveToNext())
            }
        }
    }
    private fun leerid(nombrePerfil: TextView) {
        val sql = "SELECT ID FROM USUARIOS"
        val db = databaseHelper.verPerfil(sql)

        if (db != null) {
            if (db.moveToFirst()) {
                do {
                    val id = db.getString(db.getColumnIndex("ID"))
                    nombrePerfil.append("$id")
                } while (db.moveToNext())
            }
        }
    }
}

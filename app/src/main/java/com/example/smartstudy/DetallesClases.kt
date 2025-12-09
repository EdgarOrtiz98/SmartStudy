package com.example.smartstudy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class DetallesClases : AppCompatActivity() {
    private lateinit var databaseHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_clases)

        databaseHelper = DBHelper(this)
        val id = intent.getStringExtra("id")

        val materia = findViewById<TextView>(R.id.nombreClaseDe)
        val profe = findViewById<TextView>(R.id.ProfesorClaseDe)
        val dia = findViewById<TextView>(R.id.DiaClaseDe)
        val aula = findViewById<TextView>(R.id.AulaClaseDe)
        val horaI = findViewById<TextView>(R.id.HoraIClaseDe)
        val horaF = findViewById<TextView>(R.id.HoraFClaseDe)

        val btnEliminarClase = findViewById<Button>(R.id.eliminarCLase)
        val btnEditarClase = findViewById<Button>(R.id.editarClase)

        val btnRegrasar = findViewById<ImageView>(R.id.regresarC)
        btnRegrasar.setOnClickListener {
            val intent = Intent(this, Horario::class.java)
            startActivity(intent)
        }

        val clase = obtenerDetallesTarea(id)

        if (clase != null) {
            materia.text = clase.materia
            profe.text = clase.horaF
            dia.text = clase.aula
            aula.text = clase.horaI
            horaI.text = clase.dia
            horaF.text = clase.profe

            btnEliminarClase.setOnClickListener {
                eliminarTarea(id)
            }
            btnEditarClase.setOnClickListener {
                val intent = Intent(this, NuevaClase::class.java)
                intent.putExtra("ClaseID", id)
                startActivity(intent)
            }
        }
    }

    private fun obtenerDetallesTarea(id: String?): DatalistClase? {
        return databaseHelper.obtenerDetallesClase(id)
    }

    private fun eliminarTarea(id: String?) {
        val filasEliminadas = databaseHelper.eliminarClase(id)
        if (filasEliminadas > 0) {
            Toast.makeText(this, "Clase eliminada correctamente", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Horario::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Error al eliminar la Clase", Toast.LENGTH_SHORT).show()
        }
    }
}
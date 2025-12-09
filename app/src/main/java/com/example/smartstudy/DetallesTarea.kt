package com.example.smartstudy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class DetallesTarea : AppCompatActivity() {
    private lateinit var databaseHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalles_tarea)

        databaseHelper = DBHelper(this)
        val id = intent.getStringExtra("id")

        val nombre = findViewById<TextView>(R.id.nombreTareaDe)
        val materia = findViewById<TextView>(R.id.asignaturaTareaDe)
        val fecha = findViewById<TextView>(R.id.fechaTareaDe)
        val instruccion = findViewById<TextView>(R.id.instrupcionTareaDe)
        val btnEliminarTarea = findViewById<Button>(R.id.eliminarTarea)
        val btnEditarTarea = findViewById<Button>(R.id.editarTarea)


        val btnRegrasar = findViewById<ImageView>(R.id.regresarT)
        btnRegrasar.setOnClickListener {
            val intent = Intent(this, Tareas::class.java)
            startActivity(intent)
        }

        val tarea = obtenerDetallesTarea(id)

        if (tarea != null) {
            nombre.text = tarea.nombre
            materia.text = tarea.asignatura
            fecha.text = tarea.fecha
            instruccion.text = tarea.instrucciones

            btnEliminarTarea.setOnClickListener {
                eliminarTarea(id)
            }
            btnEditarTarea.setOnClickListener {
                val intent = Intent(this, NuevaTarea::class.java)
                intent.putExtra("tareaID", id)
                startActivity(intent)
            }
        }
    }

    private fun obtenerDetallesTarea(id: String?): Tarea? {
        return databaseHelper.obtenerDetallesTarea(id)
    }

    private fun eliminarTarea(id: String?) {
        val filasEliminadas = databaseHelper.eliminarTarea(id)
        if (filasEliminadas > 0) {
            Toast.makeText(this, "Tarea eliminada correctamente", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Tareas::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Error al eliminar la tarea", Toast.LENGTH_SHORT).show()
        }
    }
}
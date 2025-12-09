package com.example.smartstudy

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NuevaTarea : AppCompatActivity() {

    private lateinit var databaseHelper: DBHelper
    private lateinit var fechaTareaEditText: TextInputEditText
    private lateinit var fechaTareaLayout: TextInputLayout
    private var calendar = Calendar.getInstance()
    private var tareaId: Int? = null

    private lateinit var nombre: TextInputLayout
    private lateinit var asignatura: TextInputLayout
    private lateinit var instruccion: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_nueva_tarea)

            databaseHelper = DBHelper(this)

            // tareaId = intent.getStringExtra("tareaID")?.toIntOrNull()
            // if (tareaId != null) {
            //     cargarDetallesTarea(tareaId!!)
            // }
            // tareaId = intent.getStringExtra("tareaID")?.toIntOrNull()
            val ID = intent.getStringExtra("tareaID")
            val tareaId = ID?.toIntOrNull()

            // if (tareaId != null) {
            //     cargarDetallesTarea(tareaId)
            // }

            fechaTareaEditText = findViewById(R.id.fechaTareaEditText)
            fechaTareaLayout = findViewById(R.id.fechaTarea)

            fechaTareaEditText.setOnClickListener {
                mostrarSelectorDeFecha()
            }

            val btnRegresar = findViewById<ImageView>(R.id.regresarTar)

            btnRegresar.setOnClickListener {
                val intent = Intent(this, Tareas::class.java)
                startActivity(intent)
            }

            nombre = findViewById(R.id.nombreTarea)
            asignatura = findViewById(R.id.asignaturaTarea)
            instruccion = findViewById(R.id.instrupcionTarea)
            val btnCrearTareas = findViewById<Button>(R.id.crearTarea)
            val alerta = findViewById<TextView>(R.id.alertaTar)
            if (tareaId != null) { cargarDetallesTarea(tareaId)}
            btnCrearTareas.setOnClickListener {
                val nombreStr = nombre.editText?.text.toString().trim()
                val asignaStr = asignatura.editText?.text.toString().trim()
                val fechStr = fechaTareaLayout.editText?.text.toString().trim()
                val instruStr = instruccion.editText?.text.toString().trim()

                if (nombreStr.isNotEmpty() && asignaStr.isNotEmpty() && fechStr.isNotEmpty() && instruStr.isNotEmpty()) {
                    if (tareaId != null) {
                        actualizarTarea(tareaId.toString(), nombreStr, asignaStr, fechStr, instruStr)
                    } else {
                        registrarTarea(nombreStr, asignaStr, fechStr, instruStr)
                    }
                } else {
                    alerta.text = "Por favor, completa todos los campos"
                    alerta.visibility = View.VISIBLE
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            finish() // Close the activity in case of an error
        }
    }

    private fun mostrarSelectorDeFecha() {
        val datePicker = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                actualizarFechaEnEditText()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePicker.show()
    }

    private fun actualizarFechaEnEditText() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        fechaTareaEditText.setText(dateFormat.format(calendar.time))
    }

    // Registra datos
    private fun registrarTarea(nombre: String, asignatura: String, fecha: String, instrucciones: String) {
        val idregistro = databaseHelper.insertarTarea(nombre, asignatura, fecha, instrucciones)
        if (idregistro != -1L) {
            Toast.makeText(this, "Registro Exitoso", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Tareas::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Error en el Registro", Toast.LENGTH_SHORT).show()
        }
    }

    // Mostrar datos en el formulario
    private fun cargarDetallesTarea(id: Int) {
        val db = databaseHelper.formUpdateTarea(id)
        if (db != null && db.moveToFirst()) {
            nombre.editText?.setText(db.getString(1).toString())
            asignatura.editText?.setText(db.getString(2).toString())
            fechaTareaLayout.editText?.setText(db.getString(3).toString())
            instruccion.editText?.setText(db.getString(4).toString())
            db.close()
        }
    }


    // actualizar datos
    private fun actualizarTarea(id: String?, nombre: String, asignatura: String, fecha: String, instrucciones: String) {
        val filasActualizadas = databaseHelper.actualizarTarea(id, nombre, asignatura, fecha, instrucciones)
        if (filasActualizadas != 0) {
            Toast.makeText(this, "Tarea actualizada correctamente", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Tareas::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Error al actualizar la tarea", Toast.LENGTH_SHORT).show()
        }
    }
}
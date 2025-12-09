package com.example.smartstudy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat


class NuevaClase : AppCompatActivity() {
    private lateinit var databaseHelper: DBHelper
    private lateinit var asignatura: TextInputLayout
    private lateinit var aula: TextInputLayout
    private lateinit var profesor: TextInputLayout
    private lateinit var diaClase: TextInputLayout
    private lateinit var horaInicio: TextInputLayout
    private lateinit var horaFin: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_clase)

        val diasDeLaSemana = arrayOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, diasDeLaSemana)

        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        autoCompleteTextView.setAdapter(adapter)

        databaseHelper = DBHelper(this)


        val ID = intent.getStringExtra("ClaseID")
        val claseId = ID?.toIntOrNull()

        asignatura = findViewById(R.id.asignaturaClase)
        aula = findViewById(R.id.aulaClase)
        profesor = findViewById(R.id.profesorClase)
        diaClase = findViewById(R.id.diaClase)
        horaInicio = findViewById(R.id.horaInicioClase)
        horaFin = findViewById(R.id.horaFinClase)

        val btnCrearClase = findViewById<Button>(R.id.btnCrearClase)
        val alerta = findViewById<TextView>(R.id.alertaClass)

        if (claseId != null) {cargarDetallesClase(claseId)}
        btnCrearClase.setOnClickListener {
            val asign = asignatura.editText?.text.toString().trim()
            val aula = aula.editText?.text.toString().trim()
            val profe = profesor.editText?.text.toString().trim()
            val dia = diaClase.editText?.text.toString().trim()
            val horaI = horaInicio.editText?.text.toString().trim()
            val horaF = horaFin.editText?.text.toString().trim()

            if (asign.isNotEmpty() && aula.isNotEmpty() && profe.isNotEmpty() && dia.isNotEmpty() && horaI.isNotEmpty() && horaF.isNotEmpty()) {
                if (claseId != null) {
                    actualizarClase(claseId.toString(), asign, aula, profe, dia, horaI, horaF)
                } else {
                    registarClase(asign, aula, profe, dia, horaI, horaF)
                }
            }  else {
                alerta.text = "Por favor, completa todos los campos"
                alerta.visibility = View.VISIBLE
            }
        }

        val btnRegrasar = findViewById<ImageView>(R.id.regresarClass)
        btnRegrasar.setOnClickListener {
            val intent = Intent(this, Horario::class.java)
            startActivity(intent)
        }

        val HoraIni = findViewById<TextInputEditText>(R.id.editTextHoraInicio)
        val iconHoraI = findViewById<ImageView>(R.id.iconHoraI)
        iconHoraI.setOnClickListener {
            showTimePicker(HoraIni)
        }
        val HoraFi = findViewById<TextInputEditText>(R.id.editTextHoraFin)
        val iconHoraF = findViewById<ImageView>(R.id.iconHoraF)
        iconHoraF.setOnClickListener {
            showTimePicker(HoraFi)
        }
    }

    private fun actualizarClase(id: String, asign: String, aula: String, profe: String, dia: String, horaI: String, horaF: String) {
        val filasActualizadas = databaseHelper.actualizarClase(id, asign, aula, profe, dia, horaI, horaF)
        if (filasActualizadas != 0) {
            Toast.makeText(this, "Clase actualizada correctamente", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Horario::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Error al actualizar la clase", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showTimePicker(editText: TextInputEditText) {
        val currentTime = System.currentTimeMillis()
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(12)
            .setMinute(0)
            .build()
        picker.addOnPositiveButtonClickListener {
            val hour = picker.hour
            val minute = picker.minute
            val formattedTime = String.format("%02d:%02d", hour, minute)
            editText.setText(formattedTime)
        }
        picker.show(supportFragmentManager, "timePicker")
    }

    private fun registarClase(asign: String, aula: String, profe: String, dia: String, horaI: String, horaF: String) {
        val clase = databaseHelper.insertarClases(asign, aula, profe, dia, horaI, horaF)
        if (clase != -1L) {
            Toast.makeText(this, "Registro Exitoso", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Horario::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Error en el Registro", Toast.LENGTH_SHORT).show()
        }
    }

    // Mostrar datos en el formulario
    private fun cargarDetallesClase(ID: Int) {
        val db = databaseHelper.formUpdateClase(ID)
        if (db != null && db.moveToFirst()) {
            asignatura.editText?.setText(db.getString(1).toString())
            aula.editText?.setText(db.getString(2).toString())
            profesor.editText?.setText(db.getString(3).toString())
            diaClase.editText?.setText(db.getString(4).toString())
            horaInicio.editText?.setText(db.getString(5).toString())
            horaFin.editText?.setText(db.getString(6).toString())
        }
    }
}
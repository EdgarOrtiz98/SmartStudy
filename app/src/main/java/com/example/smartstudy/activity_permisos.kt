package com.example.smartstudy

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

// Heredamos de MenuLogic para tener el menú funcional
class activity_permisos : MenuLogic() {

    private val CODIGO_SOLICITUD_CAMARA = 100
    private lateinit var tvEstado: TextView
    private lateinit var btnPedir: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permisos)

        // 1. Configurar Toolbar y Menú (Heredado de MenuLogic)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)

        // Llamamos a la función setup de MenuLogic
        setup(drawerLayout, toolbar, navigationView)

        // 2. Inicializar Vistas
        tvEstado = findViewById(R.id.tvEstadoPermiso)
        btnPedir = findViewById(R.id.btnPedirPermiso)

        // Verificar estado inicial al abrir la pantalla
        actualizarEstadoVisual()

        btnPedir.setOnClickListener {
            checkCameraPermission()
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {

            // Si no tenemos permiso, lo pedimos
            // Explicación racional (opcional, si el usuario ya negó una vez)
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                mostrarAlertaRacional()
            } else {
                // Pedir permiso directamente
                solicitarPermiso()
            }
        } else {
            // Ya tenemos permiso
            Toast.makeText(this, "Permiso ya concedido", Toast.LENGTH_SHORT).show()
            actualizarEstadoVisual()
        }
    }

    private fun solicitarPermiso() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CODIGO_SOLICITUD_CAMARA
        )
    }

    private fun mostrarAlertaRacional() {
        AlertDialog.Builder(this)
            .setTitle("Permiso Necesario")
            .setMessage("Esta aplicación necesita acceso a la cámara para funcionar correctamente. Por favor, concede el permiso.")
            .setPositiveButton("Aceptar") { _, _ ->
                solicitarPermiso()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
                tvEstado.text = "Estado: Denegado por el usuario"
            }
            .create()
            .show()
    }

    // Callback: Aquí recibimos la respuesta del usuario (Conceder o Denegar)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CODIGO_SOLICITUD_CAMARA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // CASO 1: CONCEDIDO
                tvEstado.text = "Estado: Permiso Concedido ✅"
                Toast.makeText(this, "¡Permiso de Cámara Concedido!", Toast.LENGTH_LONG).show()
                btnPedir.isEnabled = false
                btnPedir.text = "Permiso Activo"
            } else {
                // CASO 2: DENEGADO
                tvEstado.text = "Estado: Permiso Denegado ❌"
                mostrarAlertaDenegado()
            }
        }
    }

    private fun mostrarAlertaDenegado() {
        AlertDialog.Builder(this)
            .setTitle("Permiso Denegado")
            .setMessage("Has rechazado el permiso. Sin la cámara, no podrás usar esta funcionalidad.")
            .setPositiveButton("Entendido", null)
            .create()
            .show()
    }

    private fun actualizarEstadoVisual() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            tvEstado.text = "Estado: Permiso Concedido ✅"
            btnPedir.isEnabled = false
            btnPedir.text = "Permiso Activo"
        } else {
            tvEstado.text = "Estado: No concedido"
            btnPedir.isEnabled = true
            btnPedir.text = "Solicitar Permiso"
        }
    }
}
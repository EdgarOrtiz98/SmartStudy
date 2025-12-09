package com.example.smartstudy

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class Horario : MenuLogic() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseHelper: DBHelper
    private lateinit var newArry: ArrayList<Datalist>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horario)

        databaseHelper = DBHelper(this)

        recyclerView = findViewById(R.id.verClases)

        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        setup(drawer, toolbar, navigationView)

        val crearClase = findViewById<FloatingActionButton>(R.id.btnCrearTarea)
        crearClase.setOnClickListener {
            val intent = Intent(this, NuevaClase::class.java)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        displayClase()
    }

    private fun displayClase() {
        var newcursor: Cursor? = databaseHelper!!.verClases()
        val newArry = ArrayList<DatalistClase>()
        while (newcursor!!.moveToNext()){
            val materia = newcursor.getString(1)
            val horaI = newcursor.getString(5)
            val horaF = newcursor.getString(6)
            val aula = newcursor.getString(2)
            val dia = newcursor.getString(4)
            val id = newcursor.getString(0)
            newArry.add(DatalistClase(materia, horaI, horaF, aula, dia, id,id))
        }
        recyclerView.adapter = MyAdapterClases(newArry)
    }
}
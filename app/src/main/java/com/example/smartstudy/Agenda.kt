package com.example.smartstudy

import android.database.Cursor
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView

class Agenda : MenuLogic() {

    private lateinit var recyclerViewTarea: RecyclerView
    private lateinit var recyclerViewClase: RecyclerView
    private lateinit var databaseHelper: DBHelper
    private lateinit var newArry: ArrayList<Datalist>
    private lateinit var newArry2: ArrayList<DatalistClase>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agenda)

        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        setup(drawer, toolbar, navigationView)

        databaseHelper = DBHelper(this)

        recyclerViewTarea = findViewById(R.id.recyclerViewTareas)
        recyclerViewClase = findViewById(R.id.recyclerViewClases)

        recyclerViewTarea.layoutManager = LinearLayoutManager(this)
        recyclerViewTarea.setHasFixedSize(true)
        displayTarea()

        recyclerViewClase.layoutManager = LinearLayoutManager(this)
        recyclerViewClase.setHasFixedSize(true)
        displayClase()
    }
    private fun displayTarea() {
        val newcursor: Cursor? = databaseHelper!!.verTareas()
        newArry = ArrayList<Datalist>()
        while (newcursor!!.moveToNext()){
            val titulo = newcursor.getString(1)
            val detalles = newcursor.getString(2)
            val fecha = newcursor.getString(3)
            val id = newcursor.getString(0)
            newArry.add(Datalist(titulo, detalles, fecha, id))
        }
        recyclerViewTarea.adapter = MyAdapter(newArry)
    }
    private fun displayClase() {
        val newcursor: Cursor? = databaseHelper!!.verClases()
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
        recyclerViewClase.adapter = MyAdapterClases(newArry)
    }
}
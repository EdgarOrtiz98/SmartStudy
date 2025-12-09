package com.example.smartstudy

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class Tareas : MenuLogic() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseHelper: DBHelper
    private lateinit var newArry: ArrayList<Datalist>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_tareas)

        databaseHelper = DBHelper(this)

        recyclerView = findViewById(R.id.verTareas)
        // val adapter = CustomAdapter()

        // recyclerView.layoutManager = LinearLayoutManager(this)
        // recyclerView.adapter = adapter

        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        setup(drawer, toolbar, navigationView)

        val btnFormTarea = findViewById<FloatingActionButton>(R.id.btnCrearTarea)
        btnFormTarea.setOnClickListener {
            val intent = Intent(this, NuevaTarea::class.java)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        displayTareas()
    }

    private fun displayTareas() {
        var newcursor: Cursor? = databaseHelper!!.verTareas()
        newArry = ArrayList<Datalist>()
        while (newcursor!!.moveToNext()){
            val titulo = newcursor.getString(1)
            val detalles = newcursor.getString(2)
            val fecha = newcursor.getString(3)
            val id = newcursor.getString(0)
            newArry.add(Datalist(titulo, detalles, fecha, id))
        }
        recyclerView.adapter = MyAdapter(newArry)
    }
}
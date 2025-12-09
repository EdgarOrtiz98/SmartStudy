package com.example.smartstudy

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView // Importante importar este SearchView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartstudy.network.Movie
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class ApiMoviesActivity : MenuLogic() {

    private val viewModel: MovieViewModel by viewModels()
    private lateinit var adapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_movies)

        // 1. Configurar Menú
        val toolbar = findViewById<Toolbar>(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        setup(drawerLayout, toolbar, navigationView)

        // 2. Configurar RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.rvMovies)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MoviesAdapter(emptyList())
        recyclerView.adapter = adapter

        // 3. Configurar el Buscador (SearchView)
        val searchView = findViewById<SearchView>(R.id.svMovies)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            // Se ejecuta al presionar "Enter" o la lupa del teclado
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    viewModel.onSearchQueryChanged(query) // Actualizamos query
                    viewModel.searchMovies()              // Ejecutamos búsqueda
                    searchView.clearFocus()               // Ocultar teclado
                }
                return true
            }

            // Se ejecuta cada vez que escribes una letra
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    // Si borra todo el texto, volvemos a mostrar las populares
                    viewModel.onSearchQueryChanged("")
                    viewModel.searchMovies() // Al estar vacío, el ViewModel carga populares
                } else {
                    viewModel.onSearchQueryChanged(newText)
                }
                return true
            }
        })

        // 4. Observar cambios en la lista de películas
        lifecycleScope.launch {
            viewModel.movies.collect { listaPeliculas ->
                adapter.updateMovies(listaPeliculas)
            }
        }

        // 5. Observar errores
        lifecycleScope.launch {
            viewModel.errorMessage.collect { errorMsg ->
                if (errorMsg != null) {
                    Toast.makeText(this@ApiMoviesActivity, errorMsg, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
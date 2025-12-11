package com.example.smartstudy

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
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

    // Vistas para el manejo de estados (Carga, Error, Vacío)
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmptyState: TextView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_movies)

        // 1. Configurar UI y Menú
        val toolbar = findViewById<Toolbar>(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        setup(drawerLayout, toolbar, navigationView)

        // Inicializar vistas
        progressBar = findViewById(R.id.progressBar)
        tvEmptyState = findViewById(R.id.tvEmptyState)
        recyclerView = findViewById(R.id.rvMovies)

        // 2. Configurar RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MoviesAdapter(emptyList())
        recyclerView.adapter = adapter

        // 3. Configurar el Buscador con Validaciones (PUNTO 2 DE TU TAREA)
        val searchView = findViewById<SearchView>(R.id.svMovies)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                // "Blindaje" contra errores de input
                try {
                    val input = query?.trim() ?: ""

                    // Validación A: Texto vacío
                    if (input.isEmpty()) {
                        Toast.makeText(applicationContext, "Escribe algo para buscar", Toast.LENGTH_SHORT).show()
                        return false
                    }

                    // Validación B: Caracteres especiales (Solo letras y números permitidos)
                    // Esto evita que la app truene si meten emojis o símbolos raros
                    val regex = Regex("^[a-zA-Z0-9 ñÑáéíóúÁÉÍÓÚ]+$")
                    if (!input.matches(regex)) {
                        Toast.makeText(applicationContext, "Solo se permiten letras y números", Toast.LENGTH_SHORT).show()
                        return false
                    }

                    // Si pasa las validaciones, ejecutamos la búsqueda
                    viewModel.onSearchQueryChanged(input)
                    viewModel.searchMovies()
                    searchView.clearFocus() // Ocultar teclado

                } catch (e: Exception) {
                    // Manejo de error inesperado en la búsqueda
                    Toast.makeText(applicationContext, "Error al procesar texto", Toast.LENGTH_SHORT).show()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    // Si borra el texto, regresamos a populares
                    viewModel.onSearchQueryChanged("")
                    viewModel.getPopularMovies()
                }
                return true
            }
        })

        // 4. Observadores de Estado (PUNTO 3 DE TU TAREA)

        // A) Lista de Películas
        lifecycleScope.launch {
            viewModel.movies.collect { listaPeliculas ->
                if (listaPeliculas.isNullOrEmpty()) {
                    // Si la lista está vacía (pero no hay error ni cargando), mostramos mensaje
                    if (!viewModel.isLoading.value) {
                        mostrarEstadoVacio("No se encontraron resultados")
                    }
                    else {
                        mostrarEstadoVacio("No se encontraron resultados")
                    }
                } else {
                    // Si hay datos, mostramos la lista
                    mostrarLista(listaPeliculas)
                }
            }
        }

        // B) Estado de Carga (ProgressBar)
        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                if (isLoading) {
                    progressBar.visibility = View.VISIBLE
                    tvEmptyState.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                } else {
                    progressBar.visibility = View.GONE
                    // La visibilidad de la lista se decide en el observador de 'movies' o 'error'
                }
            }
        }

        // C) Mensajes de Error
        lifecycleScope.launch {
            viewModel.errorMessage.collect { errorMsg ->
                if (errorMsg != null) {
                    // Ocultamos lista y mostramos el error en pantalla
                    mostrarEstadoVacio(errorMsg)
                    Toast.makeText(this@ApiMoviesActivity, errorMsg, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // Funciones auxiliares para controlar la UI limpiamente
    private fun mostrarLista(movies: List<Movie>) {
        recyclerView.visibility = View.VISIBLE
        tvEmptyState.visibility = View.GONE
        adapter.updateMovies(movies)
    }

    private fun mostrarEstadoVacio(mensaje: String) {
        recyclerView.visibility = View.GONE
        tvEmptyState.visibility = View.VISIBLE
        tvEmptyState.text = mensaje
    }
}
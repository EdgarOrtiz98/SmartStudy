package com.example.smartstudy

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// 👇 IMPORTACIONES NUEVAS
import com.example.smartstudy.network.Movie
import com.example.smartstudy.network.RetrofitInstance
import com.example.smartstudy.network.TmdbError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import retrofit2.HttpException

// 1. Heredamos de ViewModel
class MovieViewModel : ViewModel() {

    // Instancia de nuestra API (del archivo RetrofitInstance)
    private val apiService = RetrofitInstance.api

    // --- ESTADO DE LA UI ---

    // ... (Tu estado _movies no cambia)
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

    // ... (Tu estado _searchQuery no cambia)
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // ... (Tu estado _isLoading no cambia)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // --- 👇 NUEVO ESTADO PARA EL MENSAJE DE ERROR ---
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()


    // --- LÓGICA / EVENTOS ---

    init {
        // Cargar las películas populares en cuanto el ViewModel se cree
        getPopularMovies()
    }

    /**
     * Obtiene las películas populares de la API
     */
    fun getPopularMovies() {
        viewModelScope.launch {
            _isLoading.value = true // Empezamos a cargar
            _errorMessage.value = null // <-- Limpiamos errores anteriores
            try {
                // 7. ¡Llamada a la API! (usando la función "suspend")
                val response = apiService.getPopularMovies()
                _movies.value = response.results // Actualizamos el estado con la lista
                Log.d("MovieViewModel", "Populares obtenidas: ${response.results.size} películas")

            } catch (e: Exception) {
                // 8. MANEJO DE ERRORES ACTUALIZADO
                Log.e("MovieViewModel", "Error al obtener populares: ${e.message}")
                _movies.value = emptyList() // Limpiamos la lista en caso de error
                handleApiError(e) // <-- Llamamos a la nueva función de error
            }
            _isLoading.value = false // Terminamos de cargar
        }
    }

    /**
     * Busca películas basado en un query
     */
    fun searchMovies() {
        // No buscar si el query está vacío
        val query = _searchQuery.value
        if (query.isBlank()) {
            getPopularMovies() // Si se borra la búsqueda, mostrar populares
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null // <-- Limpiamos errores anteriores
            try {
                // 9. ¡Llamada a la API de búsqueda! (Esta es la que Proxyman bloqueará)
                val response = apiService.searchMovies(query)
                _movies.value = response.results
                Log.d("MovieViewModel", "Búsqueda obtenida: ${response.results.size} películas")

            } catch (e: Exception) {
                // 10. MANEJO DE ERRORES ACTUALIZADO
                Log.e("MovieViewModel", "Error al buscar: ${e.message}")
                _movies.value = emptyList()
                handleApiError(e) // <-- Llamamos a la nueva función de error
            }
            _isLoading.value = false
        }
    }

    // --- 👇 NUEVA FUNCIÓN PRIVADA PARA MANEJAR ERRORES ---
    private fun handleApiError(e: Exception) {
        if (e is HttpException) {
            // Si es un error HTTP (como nuestro 404 simulado por Proxyman)
            val errorBody = e.response()?.errorBody()?.string()
            if (errorBody != null) {
                try {
                    // Intentamos "traducir" el JSON de error
                    val tmdbError = Json { ignoreUnknownKeys = true }.decodeFromString<TmdbError>(errorBody)

                    // ¡ÉXITO! Ponemos el mensaje del JSON en nuestro estado
                    _errorMessage.value = tmdbError.statusMessage
                    Log.e("MovieViewModel", "Error API parseado: ${tmdbError.statusMessage}")

                } catch (parseError: Exception) {
                    // Si el JSON de error es diferente y no se puede parsear
                    _errorMessage.value = "Error: No se pudo interpretar la respuesta del servidor."
                    Log.e("MovieViewModel", "Error al parsear errorBody: $errorBody")
                }
            } else {
                // Si no hay cuerpo de error, solo mostramos el código
                _errorMessage.value = "Error del servidor: ${e.code()}"
            }
        } else {
            // Otro tipo de error (ej. no hay internet)
            _errorMessage.value = "Error de conexión: ${e.message}"
        }
    }

    /**
     * Actualiza el estado del texto del buscador (llamado desde la UI)
     */
    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }
}
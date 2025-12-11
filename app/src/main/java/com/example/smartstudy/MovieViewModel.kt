package com.example.smartstudy

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartstudy.network.Movie
import com.example.smartstudy.network.RetrofitInstance
import com.example.smartstudy.network.TmdbError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import java.io.IOException

class MovieViewModel : ViewModel() {

    private val apiService = RetrofitInstance.api

    // --- ESTADO DE LA UI ---
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // --- LÓGICA / EVENTOS ---

    init {
        getPopularMovies()
    }

    fun getPopularMovies() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = apiService.getPopularMovies()
                _movies.value = response.results
                Log.d("MovieViewModel", "Populares obtenidas: ${response.results.size} películas")

            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error al obtener populares: ${e.message}")
                _movies.value = emptyList()
                handleApiError(e) // Llama a nuestra función blindada
            }
            _isLoading.value = false
        }
    }

    fun searchMovies() {
        val query = _searchQuery.value
        if (query.isBlank()) {
            getPopularMovies()
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = apiService.searchMovies(query)
                _movies.value = response.results
                Log.d("MovieViewModel", "Búsqueda obtenida: ${response.results.size} películas")

            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error al buscar: ${e.message}")
                _movies.value = emptyList()
                handleApiError(e) // Llama a nuestra función blindada
            }
            _isLoading.value = false
        }
    }

    // --- FUNCIÓN UNIFICADA Y ROBUSTA PARA MANEJO DE ERRORES ---
    private fun handleApiError(e: Exception) {
        when (e) {
            // 1. Error de Red (Sin internet, DNS, Timeout)
            is IOException -> {
                _errorMessage.value = "Error de Red: Verifica tu conexión a internet."
                Log.e("MovieViewModel", "Network Error: ${e.message}")
            }
            // 2. Error del Servidor (404, 500, 401, etc.)
            is HttpException -> {
                val code = e.code()
                var errorMsg = "Error del servidor: $code"

                // Intentamos leer el mensaje real del JSON de error (si existe)
                val errorBody = e.response()?.errorBody()?.string()
                if (errorBody != null) {
                    try {
                        val tmdbError = Json { ignoreUnknownKeys = true }.decodeFromString<TmdbError>(errorBody)
                        errorMsg = "API Error: ${tmdbError.statusMessage}"
                    } catch (parseError: Exception) {
                        Log.e("MovieViewModel", "No se pudo parsear el error JSON")
                    }
                }

                // Si no pudimos leer el JSON, usamos mensajes genéricos por código
                if (errorMsg.startsWith("Error del servidor")) {
                    errorMsg = when (code) {
                        401 -> "401 - Error de autenticación (API Key inválida)."
                        404 -> "404 - Recurso no encontrado."
                        500 -> "500 - Error interno del servidor de películas."
                        else -> "Error del servidor: $code"
                    }
                }

                _errorMessage.value = errorMsg
                Log.e("MovieViewModel", "Http Error $code: ${e.message}")
            }
            // 3. Otros errores (Parseo, NullPointer, etc.)
            else -> {
                _errorMessage.value = "Error desconocido: ${e.localizedMessage}"
                Log.e("MovieViewModel", "Unknown Error: ${e.message}")
            }
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }
}
package com.example.smartstudy.network

import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApiService {

    // Llama a las películas populares
    // "suspend" significa que se puede pausar y ejecutar en segundo plano
    @GET("movie/popular")
    suspend fun getPopularMovies(): MovieResponse

    // Llama al buscador
    // Le pasa el texto de búsqueda como un parámetro de la URL
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String
    ): MovieResponse
}
package com.example.smartstudy.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Esto le dice al serializador que ignore campos en el JSON
// que no hayamos definido aquí (¡muy útil!)
@Serializable
data class MovieResponse(
    val page: Int,
    val results: List<Movie>
)

@Serializable
data class Movie(
    val id: Int,
    val title: String,

    // @SerialName le dice al serializador:
    // "Cuando veas 'poster_path' en el JSON, guárdalo en la variable 'posterPath'"
    @SerialName("poster_path")
    val posterPath: String?, // La '?' significa que puede ser nulo

    @SerialName("release_date")
    val releaseDate: String?,

    val overview: String?,

    @SerialName("vote_average")
    val voteAverage: Double
)

@Serializable
data class TmdbError(
    @SerialName("status_code")
    val statusCode: Int,
    @SerialName("status_message")
    val statusMessage: String,
    val success: Boolean
)
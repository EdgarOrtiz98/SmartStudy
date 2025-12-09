package com.example.smartstudy.network

import com.example.smartstudy.BuildConfig
import kotlinx.serialization.json.Json
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import retrofit2.Retrofit

// Esto es un "Singleton", un objeto único que vive en toda la app
object RetrofitInstance {

    // La URL base de la API (la tenías en tu JS)
    private const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"

    // Un "Interceptor" es un código que "intercepta" cada llamada
    // antes de que salga a internet.
    private val authInterceptor = Interceptor { chain ->
        val newRequest = chain.request().newBuilder()
            .addHeader(
                "Authorization",
                "Bearer ${BuildConfig.TMDB_API_TOKEN}" // ¡Aquí usamos tu token!
            )
            .addHeader("Content-Type", "application/json")
            .build()
        chain.proceed(newRequest)
    }

    // Un cliente HTTP que usa nuestro interceptor
    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    // Configuración para el serializador JSON
    private val json = Json {
        ignoreUnknownKeys = true // Ignora campos que no estén en nuestras Data Classes
    }

    // El objeto Retrofit que sabe cómo hablar con la API
    private val retrofit = Retrofit.Builder()
        .baseUrl(TMDB_BASE_URL)
        .client(client) // Usa el cliente con nuestra API key
        .addConverterFactory(
            json.asConverterFactory("application/json".toMediaType())
        )
        .build()

    // Finalmente, creamos la implementación de nuestra Interfaz
    // Esto es lo que usaremos en el resto de la app
    val api: TmdbApiService by lazy {
        retrofit.create(TmdbApiService::class.java)
    }
}
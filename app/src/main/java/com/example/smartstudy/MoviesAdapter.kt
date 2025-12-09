package com.example.smartstudy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartstudy.network.Movie

class MoviesAdapter(private var movies: List<Movie>) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgPoster: ImageView = view.findViewById(R.id.imgMoviePoster)
        val txtTitle: TextView = view.findViewById(R.id.txtMovieTitle)
        // Nuevos campos
        val txtYear: TextView = view.findViewById(R.id.txtMovieYear)
        val txtOverview: TextView = view.findViewById(R.id.txtMovieOverview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]

        holder.txtTitle.text = movie.title

        // Extraemos solo el AÑO de la fecha (ej: "2023-12-01" -> "2023")
        val year = if (!movie.releaseDate.isNullOrEmpty()) {
            movie.releaseDate.take(4)
        } else {
            "N/A"
        }
        holder.txtYear.text = "Año: $year"

        // Descripción
        holder.txtOverview.text = if (!movie.overview.isNullOrEmpty()) movie.overview else "Sin descripción disponible."

        // Cargar imagen con Glide
        val imageUrl = "https://image.tmdb.org/t/p/w500/${movie.posterPath}"
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .centerCrop()
            .placeholder(android.R.drawable.ic_menu_gallery)
            .into(holder.imgPoster)
    }

    override fun getItemCount() = movies.size

    fun updateMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}
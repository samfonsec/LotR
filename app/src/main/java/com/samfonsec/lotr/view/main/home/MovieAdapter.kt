package com.samfonsec.lotr.view.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.samfonsec.lotr.data.model.Movie
import com.samfonsec.lotr.databinding.ItemMovieBinding
import kotlin.math.roundToInt

class MovieAdapter(
    private val onItemClicked: () -> Unit,
    private val onFavoriteClicked: (Movie, Boolean) -> Unit
) : ListAdapter<Movie, MovieAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onFavoriteClicked)
        holder.itemView.setOnClickListener { onItemClicked() }
    }

    class ViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie, onFavoriteClicked: (Movie, Boolean) -> Unit) {
            with(binding) {
                tvName.text = movie.name
                tvScore.text = movie.rottenTomatoesScore.roundToInt().toString()
                cbFavorite.isChecked = movie.isFavorite
                cbFavorite.setOnCheckedChangeListener { _, isChecked ->
                    onFavoriteClicked(movie, isChecked)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Movie> =
            object : DiffUtil.ItemCallback<Movie>() {
                override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                    return oldItem._id == newItem._id
                }

                override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
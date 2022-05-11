package com.samfonsec.lotr.view.characters.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.samfonsec.lotr.data.model.Character
import com.samfonsec.lotr.databinding.ItemCharacterBinding
import java.util.*

class CharacterAdapter(
    private val onItemClicked: (Character) -> Unit
) : ListAdapter<Character, CharacterAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { movie ->
            holder.bind(movie)
            holder.itemView.setOnClickListener { onItemClicked(movie) }
        }
    }

    class ViewHolder(private val binding: ItemCharacterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Character) {
            with(binding) {
                tvName.text = movie.name
                tvRace.text = movie.race.lowercase(Locale.getDefault())
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Character> =
            object : DiffUtil.ItemCallback<Character>() {
                override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
                    return oldItem._id == newItem._id
                }

                override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
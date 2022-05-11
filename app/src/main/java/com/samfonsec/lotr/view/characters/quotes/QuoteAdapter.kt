package com.samfonsec.lotr.view.characters.quotes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.samfonsec.lotr.data.model.Quote
import com.samfonsec.lotr.databinding.ItemQuoteBinding

class QuoteAdapter : ListAdapter<Quote, QuoteAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemQuoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemQuoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(quote: Quote) {
            binding.tvDialog.text = quote.dialog
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Quote> =
            object : DiffUtil.ItemCallback<Quote>() {
                override fun areItemsTheSame(oldItem: Quote, newItem: Quote): Boolean {
                    return oldItem._id == newItem._id
                }

                override fun areContentsTheSame(oldItem: Quote, newItem: Quote): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
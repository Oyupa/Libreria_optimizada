package com.example.libreria_optimizada

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NovelAdapter(
    private val novels: MutableList<Novel>,
    private val onItemClick: (Novel) -> Unit
) : RecyclerView.Adapter<NovelAdapter.NovelViewHolder>() {

    // ViewHolder para gestionar las vistas de cada elemento
    class NovelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.tvTitle)
        val authorTextView: TextView = itemView.findViewById(R.id.tvAuthor)

        // Metodo de enlace de datos para el ViewHolder
        fun bind(novel: Novel, onItemClick: (Novel) -> Unit) {
            titleTextView.text = novel.title
            authorTextView.text = novel.author
            itemView.setOnClickListener {
                onItemClick(novel) // Llama al callback cuando se selecciona un elemento
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NovelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_novel, parent, false)
        return NovelViewHolder(view)
    }

    override fun onBindViewHolder(holder: NovelViewHolder, position: Int) {
        val novel = novels[position]
        holder.bind(novel, onItemClick) // Enlazamos el item con el ViewHolder y el callback
    }

    override fun getItemCount(): Int = novels.size

    // Metodo para añadir una nueva novela a la lista
    fun addNovel(novel: Novel) {
        novels.add(novel)
        notifyItemInserted(novels.size - 1)
    }
}

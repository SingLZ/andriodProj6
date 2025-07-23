package com.example.andriodproj6

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PokemonAdapter(private var items: List<PokemonData>) :
    RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvBaseExp: TextView = itemView.findViewById(R.id.tvBaseExp)
        val tvHeight: TextView = itemView.findViewById(R.id.tvHeight)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = items[position]
        holder.tvName.text = "Name: ${pokemon.name}"
        holder.tvBaseExp.text = "Base Experience: ${pokemon.baseExperience}"
        holder.tvHeight.text = "Height: ${pokemon.height}"
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<PokemonData>) {
        items = newItems
        notifyDataSetChanged()
    }
}

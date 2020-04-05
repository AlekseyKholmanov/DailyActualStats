package com.example.dailyactualstats.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyactualstats.R

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
class SpreadAdapter(
    private val items: MutableList<Spread> = mutableListOf(),
    val context: Context
) : RecyclerView.Adapter<SpreadHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpreadHolder {
        val viewType = LayoutInflater.from(context).inflate(R.layout.item_spread, parent, false)
        return SpreadHolder(viewType)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SpreadHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(newItems:List<Spread>){
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}

class SpreadHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val country: TextView = view.findViewById(R.id.country)
    private val infected: TextView = view.findViewById(R.id.infected)

    fun bind(spread: Spread) {
        country.text = spread.country
        infected.text = spread.infected.toString()
    }
}

class Spread(val country: String, val infected: Int)

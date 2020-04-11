package com.example.dailyactualstats.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyactualstats.R
import com.example.dailyactualstats.ui.adapters.items.DetailsCoronaItem

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
class DetailsAdapter(
    private val items: MutableList<DetailsCoronaItem> = mutableListOf(),
    val context: Context
) : RecyclerView.Adapter<DetailsAdapter.DetailsHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsHolder {
        val viewType = LayoutInflater.from(context).inflate(R.layout.item_details, parent, false)
        return DetailsHolder(viewType)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: DetailsHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(newItems: List<DetailsCoronaItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    class DetailsHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val date: TextView = view.findViewById(R.id.date)
        private val infected: TextView = view.findViewById(R.id.infected)
        private val death: TextView = view.findViewById(R.id.death)

        fun bind(info: DetailsCoronaItem) {
            val infectedText = "Infected: ${info.infected}"
            val deathText = "Death: ${info.death}"
            date.text = info.date.toString("dd.MM.yyyy")
            infected.text =  infectedText
            death.text = deathText
        }
    }


}
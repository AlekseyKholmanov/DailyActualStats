package com.example.dailyactualstats.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyactualstats.R
import com.example.dailyactualstats.ui.adapters.items.CountryItem
import kotlinx.android.synthetic.main.item_country.view.*

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
class CountriesAdapter(
    private val items: MutableList<CountryItem> = mutableListOf(),
    val context: Context,
    private val countryClickListener: (String, Boolean) -> Unit
) : RecyclerView.Adapter<CountryHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryHolder {
        val viewType = LayoutInflater.from(context).inflate(R.layout.item_country, parent, false)
        return CountryHolder(viewType, countryClickListener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CountryHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(newItems: List<CountryItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}

class CountryHolder(view: View, private val countryClickListener: (String, Boolean) -> Unit) :
    RecyclerView.ViewHolder(view) {
    fun bind(country: CountryItem) {
        with(itemView) {
            countryItem.isChecked = country.isChecked
            countryItem.text = country.country
            countryItem.setOnClickListener {
                countryItem.isChecked = !countryItem.isChecked
                countryClickListener(country.code, countryItem.isChecked)
            }
        }
    }
}
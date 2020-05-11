package com.example.dailyactualstats.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dailyactualstats.R
import com.example.dailyactualstats.base.BaseFragment
import com.example.dailyactualstats.ui.adapters.CountriesAdapter
import com.example.dailyactualstats.ui.adapters.items.CountryItem
import com.example.dailyactualstats.ui.viewmodels.MultiplyViewModel
import kotlinx.android.synthetic.main.fragment_multiply_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
 class MultiplyChartFragment : BaseFragment(R.layout.fragment_multiply_details) {

    private val viewModel: MultiplyViewModel by viewModel()
    val adapterCallback = {code:String , isChecked: Boolean ->

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getCountries()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager = GridLayoutManager(requireContext(), 3, GridLayoutManager.HORIZONTAL, false)
        countries.adapter = CountriesAdapter(context = requireContext(), countryClickListener = adapterCallback)
        countries.layoutManager = manager
        viewModel.countries.observe(viewLifecycleOwner, Observer(::updateAdapter))
    }

    private fun updateAdapter(items: List<CountryItem>) {
        (countries.adapter as CountriesAdapter).setItems(items)
    }
}
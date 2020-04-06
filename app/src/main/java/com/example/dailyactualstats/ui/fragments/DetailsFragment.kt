package com.example.dailyactualstats.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.example.dailyactualstats.R
import com.example.dailyactualstats.base.BaseFragment
import com.example.dailyactualstats.models.db.CountryEntity
import com.example.dailyactualstats.ui.adapters.DetailsAdapter
import com.example.dailyactualstats.ui.viewmodels.DetailsViewModel
import kotlinx.android.synthetic.main.fragment_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
class DetailsFragment:BaseFragment(R.layout.fragment_details) {

    private val args:DetailsFragmentArgs by navArgs()

    private val viewModel:DetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getCountryInfo(args.countryCode)
        viewModel.getSpreadInfo(args.countryCode)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.country.observe(viewLifecycleOwner, Observer (::setCountryInfo))
        viewModel.info.observe(viewLifecycleOwner, Observer (::setSpreadInfo))
        val adapter = DetailsAdapter(context = requireContext())
        detailsRecyclerView.adapter = adapter
    }

    private fun setSpreadInfo(triple: Triple<List<DetailsAdapter.DetailsInfo>, Int, Int>) {
        val infected = "Total infected: ${triple.second}"
        val death = "Total death: ${triple.third}"
        infectedTotal.text = infected
        deathTotal.text = death
        (detailsRecyclerView.adapter as DetailsAdapter).setItems(triple.first)

    }

    private fun setCountryInfo(entity: CountryEntity) {
        collapsingToolbar.title = entity.id
//        countryName.text = entity.id
        flag.load(entity.flagUrl)
    }
}
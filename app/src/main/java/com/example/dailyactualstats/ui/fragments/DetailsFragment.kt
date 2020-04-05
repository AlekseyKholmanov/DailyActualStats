package com.example.dailyactualstats.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.dailyactualstats.R
import com.example.dailyactualstats.base.BaseFragment
import com.example.dailyactualstats.models.db.CountryEntity
import com.example.dailyactualstats.ui.adapters.DetailsAdapter
import com.example.dailyactualstats.ui.viewmodels.DetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
class DetailsFragment:BaseFragment(R.layout.fragment_details) {

    private val args:DetailsFragmentArgs by navArgs()

    private val viewmodel:DetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel.getCountryInfo(args.countryCode)
        viewmodel.getSpreadInfo(args.countryCode)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.country.observe(viewLifecycleOwner, Observer (::setCountryInfo))
        viewmodel.info.observe(viewLifecycleOwner, Observer (::setSpreadInfo))
    }

    private fun setSpreadInfo(triple: Triple<List<DetailsAdapter.DetailsInfo>, Int, Int>) {
        TODO("Not yet implemented")
    }

    private fun setCountryInfo(entity: CountryEntity) {
        TODO("Not yet implemented")
    }
}
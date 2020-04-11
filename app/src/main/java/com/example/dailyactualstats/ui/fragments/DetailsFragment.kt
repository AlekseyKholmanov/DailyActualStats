package com.example.dailyactualstats.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.ImageLoader
import coil.api.load
import com.example.dailyactualstats.R
import com.example.dailyactualstats.base.BaseFragment
import com.example.dailyactualstats.models.db.CountryEntity
import com.example.dailyactualstats.ui.adapters.DetailsAdapter
import com.example.dailyactualstats.ui.adapters.items.DetailsCoronaItem
import com.example.dailyactualstats.ui.viewmodels.DetailsViewModel
import kotlinx.android.synthetic.main.fragment_details.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
class DetailsFragment : BaseFragment(R.layout.fragment_details) {

    private val args: DetailsFragmentArgs by navArgs()

    private val viewModel: DetailsViewModel by viewModel()
    private val imageLoader: ImageLoader by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getCountryInfo(args.countryCode)
        viewModel.getSpreadInfo(args.countryCode)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        viewModel.country.observe(viewLifecycleOwner, Observer(::setCountryInfo))
        viewModel.info.observe(viewLifecycleOwner, Observer(::setSpreadInfo))
        val adapter = DetailsAdapter(context = requireContext())
        detailsRecyclerView.adapter = adapter
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.openCharts -> {
                val direction = DetailsFragmentDirections.actionDetailsFragmentToChartFragment(args.countryCode)
                findNavController().navigate(direction)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun setSpreadInfo(triple: Triple<List<DetailsCoronaItem>, Int, Int>) {
        val emojiDead = String(Character.toChars(0x1F480))
        val emojiInfected = String(Character.toChars(0x1F912))
        val infected = "Total $emojiInfected    : ${triple.second}"
        val death = "Total $emojiDead: ${triple.third}"
        infectedTotal.text = infected
        deathTotal.text = death
        (detailsRecyclerView.adapter as DetailsAdapter).setItems(triple.first)

    }

    private fun setCountryInfo(entity: CountryEntity) {
        collapsingToolbar.title = entity.id
        flag.load(entity.flagUrl, imageLoader)
    }
}
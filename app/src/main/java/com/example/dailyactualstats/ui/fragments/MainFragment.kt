package com.example.dailyactualstats.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.dailyactualstats.R
import com.example.dailyactualstats.base.BaseFragment
import com.example.dailyactualstats.ui.adapters.SpreadAdapter
import com.example.dailyactualstats.ui.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
class MainFragment : BaseFragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModel()

    private val countryClickListener = { countryCode: String ->
        val destination = MainFragmentDirections.actionMainFragmentToDetailsFragment(countryCode)
        findNavController().navigate(destination)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getInfo()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        viewModel.info.observe(viewLifecycleOwner, Observer(::updateInfo))
        val adapter =
            SpreadAdapter(context = requireContext(), countryClickListener = countryClickListener)
        spreadRecyclerView.adapter = adapter
        refreshLayout.setOnRefreshListener {
            viewModel.getInfo(force = true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.openCharts -> {
                val direction = MainFragmentDirections.actionMainFragmentToChartFragment("")
                findNavController().navigate(direction)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateInfo(state: MainViewModel.ViewState) {
        refreshLayout.isRefreshing = state.loading

        val items = state.success ?: return
        totalCount.text = items.sumBy { it.infected }.toString()
        (spreadRecyclerView.adapter as SpreadAdapter).setItems(items)
    }
}
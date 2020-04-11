package com.example.dailyactualstats.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.dailyactualstats.R
import com.example.dailyactualstats.base.BaseFragment
import com.example.dailyactualstats.ui.adapters.Spread
import com.example.dailyactualstats.ui.adapters.SpreadAdapter
import com.example.dailyactualstats.ui.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
class MainFragment : BaseFragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getInfo()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.info.observe(viewLifecycleOwner, Observer(::updateInfo))
        val adapter = SpreadAdapter(context = requireContext(), countryClickListener = countryClickListener)
        spreadRecyclerView.adapter = adapter
        refreshLayout.setOnRefreshListener {
            viewModel.getInfo(force = true)
        }
    }

    private val countryClickListener = { countryCode: String ->
        val destination = MainFragmentDirections.actionMainFragmentToDetailsFragment(countryCode)
        findNavController().navigate(destination)
    }

    private fun updateInfo(state: MainViewModel.ViewState) {
        refreshLayout.isRefreshing = state.loading

        val items = state.success ?: return
        totalCount.text = items.sumBy { it.infected }.toString()
        (spreadRecyclerView.adapter as SpreadAdapter).setItems(items)
    }
}
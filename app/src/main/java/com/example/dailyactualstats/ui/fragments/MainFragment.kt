package com.example.dailyactualstats.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
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
class MainFragment:BaseFragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.info.observe(viewLifecycleOwner, Observer(::updateInfo))
        val adapter = SpreadAdapter(context = requireContext())
        spreadRecyclerView.adapter = adapter
    }

    private fun updateInfo(spreads: List<Spread>) {
        Log.d("M_MainFragment","size: ${spreads.size}")
        (spreadRecyclerView.adapter as SpreadAdapter).setItems(spreads)
    }
}
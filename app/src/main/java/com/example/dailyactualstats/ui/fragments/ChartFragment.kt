package com.example.dailyactualstats.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.dailyactualstats.R
import com.example.dailyactualstats.base.BaseFragment
import com.example.dailyactualstats.ui.adapters.items.DetailsCoronaItem
import com.example.dailyactualstats.ui.viewmodels.ChartViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.fragment_chart.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
class ChartFragment:BaseFragment(R.layout.fragment_chart) {

    private val args: ChartFragmentArgs by navArgs()

    private val viewModel: ChartViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getInfo(args.CountryCode)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        viewModel.info.observe(viewLifecycleOwner, Observer(::updateChart))
        with(lineCharts){
            setBackgroundColor(Color.WHITE)
            description.text = "Total infected per day"
            setTouchEnabled(false)
            setDrawGridBackground(false)
            isDragEnabled = false
            setScaleEnabled(false)
        }
    }

    private fun updateChart(info: List<Entry>) {
        val set = LineDataSet(info, "Infected per day in world  ${args.CountryCode}").apply {
            setDrawIcons(false)
            color = Color.BLACK
            lineWidth = 1f
            circleRadius = 2f
            setDrawCircleHole(false)
            valueTextSize = 5f
            isHighlightEnabled = false
        }
        val dataset = ArrayList<ILineDataSet>()
        dataset.add(set)
        val lineData = LineData(dataset)
        lineCharts.data = lineData
        lineCharts.invalidate()
    }
}
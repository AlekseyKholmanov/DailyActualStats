package com.example.dailyactualstats.ui.fragments

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.dailyactualstats.R
import com.example.dailyactualstats.base.BaseFragment
import com.example.dailyactualstats.ui.adapters.items.DetailsCoronaItem
import com.example.dailyactualstats.ui.custom.Marker
import com.example.dailyactualstats.ui.dialogs.ProgressDialog
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
class ChartFragment : BaseFragment(R.layout.fragment_chart) {

    private val args: ChartFragmentArgs by navArgs()

    private val viewModel: ChartViewModel by viewModel()

    private var dialog: Dialog? = null

    val markers = mutableListOf<Marker>().apply {
        this.add(Marker(value = 10))
        this.add(Marker(value = 100))
        this.add(Marker(value = 400))
        this.add(Marker(value = 130))
        this.add(Marker(value = 45))
        this.add(Marker(value = 5))
        this.add(Marker(value = 20))
        this.add(Marker(value = 442))
        this.add(Marker(value = 400))
        this.add(Marker(value = 22))
        this.add(Marker(value = 104))
        this.add(Marker(value = 103))
        this.add(Marker(value = 65))
        this.add(Marker(value = 22))
    }

    val weeks = mutableListOf<String>().apply {
        this.add("week 1")
        this.add("week 2")
        this.add("week 3")
        this.add("week 4")
        this.add("week 5")
        this.add("week 6")
        this.add("week 7")
        this.add("week 8")
        this.add("week 9")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getInfo(args.CountryCode)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        viewModel.info.observe(viewLifecycleOwner, Observer(::renderState))
        customChart.setMarkersAndWeeks(markers, weeks)
        with(lineChartsInfected) {
            setBackgroundColor(Color.WHITE)
            description.text = "infected per day"
            setTouchEnabled(false)
            setDrawGridBackground(false)
            isDragEnabled = false
            setScaleEnabled(false)
            setNoDataText("")
        }
        with(lineChartsDead) {
            setBackgroundColor(Color.WHITE)
            description.text = "dead per day"
            setTouchEnabled(false)
            setDrawGridBackground(false)
            isDragEnabled = false
            setScaleEnabled(false)
            setNoDataText("")
        }
    }

    private fun renderState(state: ChartViewModel.ChartViewState) {
        renderLoading(state.loading)
        renderSuccess(state.success)
    }

    private fun renderSuccess(success: List<DetailsCoronaItem>?) {
        updateChart(success ?: return)
    }

    private fun renderLoading(loading: Boolean) {
        if (loading) {
            dialog = ProgressDialog(requireContext())
            dialog?.show()
        } else {
            dialog?.dismiss()
        }
    }

    private fun updateChart(info: List<DetailsCoronaItem>) {

        val infected = info
            .mapIndexed { index, detailsCoronaItem ->
                Entry(
                    index.toFloat(),
                    detailsCoronaItem.infected.toFloat()
                )
            }
        val dead = info.mapIndexed { index, detailsCoronaItem ->
            Entry(
                index.toFloat(),
                detailsCoronaItem.death.toFloat()
            )
        }
        val infectedSet = getDataSet(infected, "infected per day")
        val deadSet = getDataSet(dead, "dead per day")
        val infectedDataSet = ArrayList<ILineDataSet>()
        val deadDataSet = ArrayList<ILineDataSet>()
        infectedDataSet.add(infectedSet)
        deadDataSet.add(deadSet)

        val infectedLineData = LineData(infectedDataSet)
        val deadLineData = LineData(deadSet)
        lineChartsInfected.data = infectedLineData
        lineChartsDead.data = deadLineData

        lineChartsInfected.invalidate()
        lineChartsDead.invalidate()
    }

    private fun getDataSet(entries: List<Entry>, title: String): LineDataSet {
        return LineDataSet(entries, "$title  ${args.CountryCode}").apply {
            setDrawIcons(false)
            color = Color.BLACK
            lineWidth = 1f
            circleRadius = 1f
            setDrawCircleHole(false)
            valueTextSize = 5f
            isHighlightEnabled = false
        }
    }
}
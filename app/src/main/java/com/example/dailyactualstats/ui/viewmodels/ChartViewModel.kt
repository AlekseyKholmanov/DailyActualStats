package com.example.dailyactualstats.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyactualstats.repository.SpreadRepository
import com.example.dailyactualstats.ui.adapters.items.DetailsCoronaItem
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
class ChartViewModel(
    private val spreadRepository: SpreadRepository
) : ViewModel() {

    private val _info = MutableLiveData<ChartViewState>()
    val info: LiveData<ChartViewState> = _info

    fun getInfo(countyCode: String = "") {
        _info.value = ChartViewState(loading = true)
        viewModelScope.launch {
            val values = withContext(Dispatchers.IO) {
                spreadRepository.getSpreadInfo()
                    .asSequence()
                    .filter {
                        if (countyCode == "") true
                        else it.countryCode == countyCode
                    }
                    .sortedBy { it.date }
                    .groupBy { it.date }
                    .map {
                        DetailsCoronaItem(date = it.key,
                            infected = it.value.sumBy { it.cases },
                            death = it.value.sumBy { it.deaths })
                    }
            }
            _info.value = ChartViewState(loading = false, success = values)
        }
    }
    class ChartViewState(
        val loading: Boolean = false,
        val success: List<DetailsCoronaItem>? = null
    )
}


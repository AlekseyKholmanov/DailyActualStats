package com.example.dailyactualstats.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyactualstats.repository.SpreadRepository
import com.example.dailyactualstats.ui.adapters.items.CountryItem
import com.example.dailyactualstats.ui.adapters.items.DetailsCoronaItem
import com.example.dailyactualstats.ui.custom.Marker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
class MultiplyViewModel(
    private val spreadRepository: SpreadRepository
) : ViewModel() {

    private val _countries = MutableLiveData<List<CountryItem>>()
    val countries: LiveData<List<CountryItem>> = _countries


    private val _addedItem = MutableLiveData<Map<String, List<Marker>>>()
    val addedItem: LiveData<Map<String, List<Marker>>> = _addedItem


    private var allCases = mapOf<String, List<Marker>>()

    fun getCountries() {
        viewModelScope.launch(Dispatchers.IO) {
            val resp = spreadRepository.getSpreadInfo()
            val info = resp
                .sortedBy { it.date }
                .groupBy { it.countryCode }
                .mapValues {
                    val index = it.value
                        .indexOfFirst { it.cases > 0 }
                    return@mapValues it.value.takeLast(it.value.size - index)
                        .map { Marker(value = it.cases) }
                }
            val countries  = resp.groupBy { it.country to it.countryCode }
                .map { CountryItem(it.key.first, it.key.second) }

            withContext(Dispatchers.Main){
                allCases = info
                _countries.value = countries
            }
        }
    }

}
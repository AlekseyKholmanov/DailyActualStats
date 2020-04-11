package com.example.dailyactualstats.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyactualstats.api.CountryService
import com.example.dailyactualstats.models.api.CountryServiceResponse
import com.example.dailyactualstats.repository.SpreadRepository
import com.example.dailyactualstats.ui.adapters.Spread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
class MainViewModel(
    private val spreadRepository: SpreadRepository,
    private val countryService: CountryService
) : ViewModel() {



    private val _info = MutableLiveData(ViewState())
    val info: LiveData<ViewState> = _info

    private val _country = MutableLiveData<List<CountryServiceResponse>>()
    val country: LiveData<List<CountryServiceResponse>> = _country


    fun getInfo(force: Boolean = false) {
        _info.value = ViewState(loading = true)
        viewModelScope.launch {
            val resp = withContext(Dispatchers.IO) {
                spreadRepository.getSpreadInfo(force)
                    .groupBy {
                        it.country
                    }.map { (country, statistic) ->
                        Spread(country, statistic.sumBy { it.cases }, statistic.first().countryCode)
                    }.sortedByDescending { it.infected }
            }
            _info.value = ViewState(loading = false, success = resp)
        }
    }

    class ViewState(
        val loading: Boolean = false,
        val success: List<Spread>? = null
    )
}
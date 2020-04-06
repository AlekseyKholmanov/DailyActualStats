package com.example.dailyactualstats.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyactualstats.api.CountryService
import com.example.dailyactualstats.api.SpreadService
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

    init {
        getInfo()
//        getCountry()
    }

    private val _info = MutableLiveData<List<Spread>>()
    val info: LiveData<List<Spread>> = _info

    private val _country = MutableLiveData<List<CountryServiceResponse>>()
    val country: LiveData<List<CountryServiceResponse>> = _country


    private fun getInfo() {
        viewModelScope.launch {
            val resp = withContext(Dispatchers.IO) {
                spreadRepository.getSpreadInfo().groupBy {
                    it.country
                }.map { (country, statistic) ->
                    Spread(country, statistic.sumBy { it.cases }, statistic.first().countryCode)
                }.sortedByDescending { it.infected }
            }
            _info.value = resp
        }
    }

    private fun getCountry(){
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO){
                countryService.getCountriesInfo()
                    .map { Spread(it.country, it.population, it.code) }
            }
            _info.value = response
        }
    }
}
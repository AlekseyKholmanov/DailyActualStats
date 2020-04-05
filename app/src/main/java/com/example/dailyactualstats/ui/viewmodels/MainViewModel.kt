package com.example.dailyactualstats.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyactualstats.api.VirusSpreadService
import com.example.dailyactualstats.models.api.SpreadResponce
import com.example.dailyactualstats.ui.adapters.Spread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
class MainViewModel(private val api:VirusSpreadService) :ViewModel() {
    init {
        getInfo()
    }
    private val _info = MutableLiveData<List<Spread>>()
    val info:LiveData<List<Spread>> = _info

    private fun getInfo(){
        viewModelScope.launch {
            val resp = withContext(Dispatchers.IO){
                api.getSpread().payload.groupBy {
                    it.country
                }.map {(country, statistic) ->
                    Spread(country,statistic.sumBy { it.cases })
                }.sortedByDescending { it.infected }
            }
            _info.value = resp
        }
    }
}
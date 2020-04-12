package com.example.dailyactualstats.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyactualstats.models.db.CountryEntity
import com.example.dailyactualstats.repository.CountryRepository
import com.example.dailyactualstats.repository.SpreadRepository
import com.example.dailyactualstats.ui.adapters.items.DetailsCoronaItem
import com.example.dailyactualstats.ui.adapters.items.toItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
class DetailsViewModel(
    private val countryRepository: CountryRepository,
    private val spreadRepository: SpreadRepository
) : ViewModel() {

    private val _spreadState = MutableLiveData<DetailsSpreadState>()
    val spreadState: LiveData<DetailsSpreadState> = _spreadState

    private val _country = MutableLiveData<CountryEntity>()
    val country: LiveData<CountryEntity> = _country

    fun getSpreadInfo(countryCode: String) {
        _spreadState.value = DetailsSpreadState(loading = true)
        viewModelScope.launch {
            val resp = withContext(Dispatchers.IO) {
                val info = spreadRepository.getSpreadInfo()
                    .filter { it.countryCode == countryCode }
                return@withContext Triple(info.map {
                    it.toItem()
                }, info.sumBy { it.cases }, info.sumBy { it.deaths })
            }
            _spreadState.value = DetailsSpreadState(loading = false, success = resp)
        }
    }

    fun getCountryInfo(countryCode: String) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                val country = countryRepository.getCountryInfo()
                return@withContext country.first { it.code == countryCode }
            }
            _country.value = response
        }
    }

    class DetailsSpreadState(
        val loading:Boolean = false,
        val success: Triple<List<DetailsCoronaItem>, Int, Int>? = null
    )
}
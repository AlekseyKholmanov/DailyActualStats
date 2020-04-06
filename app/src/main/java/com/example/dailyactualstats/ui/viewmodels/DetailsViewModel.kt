package com.example.dailyactualstats.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyactualstats.models.db.CountryEntity
import com.example.dailyactualstats.repository.CountryRepository
import com.example.dailyactualstats.repository.SpreadRepository
import com.example.dailyactualstats.ui.adapters.DetailsAdapter
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

    private val _info = MutableLiveData<Triple<List<DetailsAdapter.DetailsInfo>, Int, Int>>()
    val info: LiveData<Triple<List<DetailsAdapter.DetailsInfo>, Int, Int>> = _info

    private val _country = MutableLiveData<CountryEntity>()
    val country: LiveData<CountryEntity> = _country

    fun getCountryInfo(countryCode: String) {
        viewModelScope.launch {
            val resp = withContext(Dispatchers.IO) {
                val info = spreadRepository.getSpreadInfo()
                    .filter { it.countryCode == countryCode }
                return@withContext Triple(info.map {
                    DetailsAdapter.DetailsInfo(
                        it.date,
                        it.deaths,
                        it.cases
                    )
                }, info.sumBy { it.cases }, info.sumBy { it.deaths })
            }
            _info.value = resp
        }
    }

    fun getSpreadInfo(countryCode: String) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                val country = countryRepository.getCountryInfo()
                   return@withContext country.first { it.code == countryCode }
            }
            _country.value = response
        }
    }
}
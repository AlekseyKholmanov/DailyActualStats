package com.example.dailyactualstats.di

import com.example.dailyactualstats.ui.viewmodels.ChartViewModel
import com.example.dailyactualstats.ui.viewmodels.DetailsViewModel
import com.example.dailyactualstats.ui.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { DetailsViewModel(get(), get()) }
    viewModel { ChartViewModel(get()) }
}

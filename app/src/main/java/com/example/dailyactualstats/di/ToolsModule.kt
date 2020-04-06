package com.example.dailyactualstats.di

import coil.ImageLoader
import coil.decode.SvgDecoder
import org.koin.dsl.module

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
val toolsmodule = module {
    single {
        ImageLoader(get()){
            componentRegistry {
                add(SvgDecoder(get()))
            }
        }
    }
}
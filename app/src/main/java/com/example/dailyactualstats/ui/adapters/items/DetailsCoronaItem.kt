package com.example.dailyactualstats.ui.adapters.items

import com.example.dailyactualstats.models.db.SpreadEntity
import org.joda.time.DateTime

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
class DetailsCoronaItem(
    val date: DateTime,
    val death: Int,
    val infected: Int
)

fun SpreadEntity.toItem(): DetailsCoronaItem {
    return DetailsCoronaItem(date = date, death = deaths, infected = cases)
}
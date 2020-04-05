package com.example.dailyactualstats.database

import androidx.room.TypeConverter
import org.joda.time.DateTime

/**
 * @author Alexey Kholmanov (alexey.holmanov@cleverpumpkin.ru)
 */
class Converters {
    @TypeConverter
    fun fromDateTime(dateTime: DateTime): Long {
        return dateTime.millis
    }

    @TypeConverter
    fun dateToDateTime(date: Long): DateTime {
        return DateTime(date)
    }
}
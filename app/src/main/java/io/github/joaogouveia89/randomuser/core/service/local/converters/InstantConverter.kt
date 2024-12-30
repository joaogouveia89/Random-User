package io.github.joaogouveia89.randomuser.core.service.local.converters

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

class InstantConverter {
    @TypeConverter
    fun fromInstant(value: Instant?): Long? {
        return value?.toEpochMilliseconds()
    }

    @TypeConverter
    fun toInstant(ms: Long?): Instant? {
        return ms?.let { Instant.fromEpochMilliseconds(it) }
    }
}
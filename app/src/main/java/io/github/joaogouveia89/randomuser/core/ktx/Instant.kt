package io.github.joaogouveia89.randomuser.core.ktx

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

fun Instant.calculateOffset(offset: String): Instant? {
    val offsetTimeValidationRegex = """^[+-](?:[0-9]|[01][0-9]|2[0-3]):[0-5][0-9]$""".toRegex()

    if(!offset.matches(offsetTimeValidationRegex)) return null
    val isSum = offset.first() != '-'
    val offsetNoSignal = offset.substring(1 until offset.length)
    val offsetSplit = offsetNoSignal.split(":")

    return if (isSum) this.plus(
        offsetSplit.first().toInt().hours
    ).plus(offsetSplit.last().toInt().minutes)
    else this.minus(
        offsetSplit.first().toInt().hours
    ).minus(offsetSplit.last().toInt().minutes)
}

fun Instant.hadPassedOneMinute(compareToInstant: Instant): Boolean {
    val compareToTime = compareToInstant.toLocalDateTime(TimeZone.currentSystemDefault())
    val thisTime = this.toLocalDateTime(TimeZone.currentSystemDefault())
    return compareToTime.minute != thisTime.minute
}

fun Instant.humanizedHourMin(): String {
    val totalSeconds = this.epochSeconds % (24 * 3600) // Seconds since the start of the UTC day
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    return "%02d:%02d".format(hours, minutes)
}

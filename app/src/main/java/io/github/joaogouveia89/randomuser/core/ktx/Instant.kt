package io.github.joaogouveia89.randomuser.core.ktx

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

fun Instant.calculateOffset(offset: String): Instant {
    val isSum = offset.first() != '-'
    var offsetNoSignal = offset.substring(1 until offset.length)
    val offsetSplit = offsetNoSignal.split(":")

    return if (isSum) this.plus(
        offsetSplit.first().toInt().hours
    )
        .plus(offsetSplit.last().toInt().minutes)
    else this.minus(
        offsetSplit.first().toInt().hours
    )
        .minus(offsetSplit.last().toInt().minutes)
}

fun Instant.hadPassedOneMinute(): Boolean {
    val systemTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val instantTime = this.toLocalDateTime(TimeZone.currentSystemDefault())
    return systemTime.minute != instantTime.minute
}

fun Instant.humanizedHourMin(): String {
    val totalSeconds = this.epochSeconds % (24 * 3600) // Seconds since the start of the UTC day
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    return "%02d:%02d".format(hours, minutes)
}

package io.github.joaogouveia89.randomuser.ktx

import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

fun Instant.calculateOffset(offset: String): Instant {
    val isSum = offset.first() == '+'
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
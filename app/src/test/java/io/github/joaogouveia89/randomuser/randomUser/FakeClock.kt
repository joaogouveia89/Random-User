package io.github.joaogouveia89.randomuser.randomUser

import kotlinx.datetime.*
import kotlin.time.Duration

class FakeClock(private var fixedInstant: Instant) : Clock {
    override fun now(): Instant = fixedInstant

    fun advanceBy(duration: Duration) {
        fixedInstant = fixedInstant.plus(duration)
    }
}

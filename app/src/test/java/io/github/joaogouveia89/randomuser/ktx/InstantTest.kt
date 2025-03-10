package io.github.joaogouveia89.randomuser.ktx

import io.github.joaogouveia89.randomuser.core.ktx.calculateOffset
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.junit.Test
import java.time.temporal.ChronoUnit
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes


class InstantTest {

    @Test
    fun `test offset with positive time should add the correct hours and minutes`() {
        val initialInstant = Clock.System.now()
        val offset = "+07:30"

        val result = initialInstant.calculateOffset(offset)

        val expectedInstant = initialInstant.plus(7.hours).plus(30.minutes)
        assertEquals(expectedInstant, result)
    }

    @Test
    fun `test offset with negative time should subtract the correct hours and minutes`() {
        val initialInstant = Clock.System.now()
        val offset = "-05:45"

        val result = initialInstant.calculateOffset(offset)

        val expectedInstant = initialInstant.minus(5.hours).minus(45.minutes)
        assertEquals(expectedInstant, result)
    }

    @Test
    fun `test offset with single digit hour should add correct time`() {
        val initialInstant = Clock.System.now()
        val offset = "+7:05"

        val result = initialInstant.calculateOffset(offset)

        val expectedInstant = initialInstant.plus(7.hours).plus(5.minutes)
        assertEquals(expectedInstant, result)
    }

    @Test
    fun `test invalid offset format should return null`() {
        val initialInstant = Clock.System.now()
        val offset = "+25:30"

        val result = initialInstant.calculateOffset(offset)

        assertNull(result)
    }

    @Test
    fun `test missing sign in offset should return null`() {
        val initialInstant = Clock.System.now()
        val offset = "07:30"

        val result = initialInstant.calculateOffset(offset)

        assertNull(result)
    }
}
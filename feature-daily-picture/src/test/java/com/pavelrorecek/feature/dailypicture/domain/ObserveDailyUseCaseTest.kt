package com.pavelrorecek.feature.dailypicture.domain

import com.pavelrorecek.feature.dailypicture.model.Daily
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class ObserveDailyUseCaseTest {

    @Test
    fun `should observe daily via repository`() {
        val daily: Daily = mockk()
        val repository: DailyPictureRepository = mockk {
            every { observe() } returns flowOf(daily)
        }

        runTest {
            repository.observe().first() shouldBe daily
        }
    }
}

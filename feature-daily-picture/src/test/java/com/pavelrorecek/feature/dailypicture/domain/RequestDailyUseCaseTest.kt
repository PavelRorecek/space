package com.pavelrorecek.feature.dailypicture.domain

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class RequestDailyUseCaseTest {

    @Test
    fun `should request daily via repository`() {
        val repository: DailyPictureRepository = mockk(relaxUnitFun = true)
        val request = RequestDailyUseCase(repository)

        runTest { request() }

        coVerify { repository.load() }
    }
}

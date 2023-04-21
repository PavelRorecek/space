package com.pavelrorecek.feature.dailypicture.domain

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class LoadDailyUseCaseTest {

    @Test
    fun `should load daily via repository`() {
        val repository: DailyPictureRepository = mockk(relaxUnitFun = true)
        val load = LoadDailyUseCase(repository)

        runTest { load() }

        coVerify { repository.load() }
    }
}

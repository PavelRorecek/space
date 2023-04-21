package com.pavelrorecek.feature.dailypicture.platform

import android.content.Context
import com.pavelrorecek.feature.dailyPicture.R
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

internal class DailyStringsImplTest {

    @Test
    fun `should return today string`() {
        val context: Context = mockk {
            every { getString(R.string.dailypicture_today) } returns "Today"
        }
        val strings = DailyStringsImpl(context)

        strings.today() shouldBe "Today"
    }

    @Test
    fun `should return explanation string`() {
        val context: Context = mockk {
            every { getString(R.string.dailypicture_explanation) } returns "Explanation"
        }
        val strings = DailyStringsImpl(context)

        strings.explanation() shouldBe "Explanation"
    }
}

package com.pavelrorecek.core.navigation.data

import com.pavelrorecek.core.navigation.domain.CurrentScreenRepository
import com.pavelrorecek.core.navigation.model.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

internal class CurrentScreenRepositoryImpl : CurrentScreenRepository {

    private val screen: MutableStateFlow<Screen?> = MutableStateFlow(null)

    override fun store(screen: Screen) {
        this.screen.value = screen
    }

    override fun observe() = screen.filterNotNull()
}

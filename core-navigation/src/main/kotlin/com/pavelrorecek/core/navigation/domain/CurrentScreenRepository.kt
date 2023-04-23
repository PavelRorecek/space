package com.pavelrorecek.core.navigation.domain

import com.pavelrorecek.core.navigation.model.Screen
import kotlinx.coroutines.flow.Flow

internal interface CurrentScreenRepository {
    fun store(screen: Screen)
    fun observe(): Flow<Screen>
}

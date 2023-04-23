package com.pavelrorecek.core.navigation.domain

import com.pavelrorecek.core.navigation.model.Screen
import kotlinx.coroutines.flow.Flow

public class ObserveCurrentScreenUseCase internal constructor(
    private val repository: CurrentScreenRepository,
) {

    public operator fun invoke(): Flow<Screen> {
        return repository.observe()
    }
}

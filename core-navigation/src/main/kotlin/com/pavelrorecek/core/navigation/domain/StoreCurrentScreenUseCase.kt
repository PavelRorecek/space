package com.pavelrorecek.core.navigation.domain

import com.pavelrorecek.core.navigation.model.Screen

public class StoreCurrentScreenUseCase internal constructor(
    private val repository: CurrentScreenRepository,
) {

    public operator fun invoke(screen: Screen) {
        repository.store(screen)
    }
}

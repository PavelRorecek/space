package com.pavelrorecek.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.pavelrorecek.core.design.AppTheme
import com.pavelrorecek.core.navigation.domain.ObserveCurrentScreenUseCase
import com.pavelrorecek.core.navigation.domain.StoreCurrentScreenUseCase
import com.pavelrorecek.core.navigation.model.Screen
import com.pavelrorecek.core.navigation.model.Screen.DAILY
import com.pavelrorecek.core.navigation.model.Screen.LAUNCHES
import com.pavelrorecek.feature.dailypicture.ui.DailyScreen
import com.pavelrorecek.feature.navigationbar.ui.NavigationBar
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

public class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: MainViewModel = koinViewModel()

            AppTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                ) {
                    val navController = rememberAnimatedNavController()

                    AnimatedNavHost(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        navController = navController,
                        startDestination = DAILY.name,
                        enterTransition = { EnterTransition.None },
                        exitTransition = { ExitTransition.None },
                    ) {
                        composable(DAILY.name) { DailyScreen() }
                        composable(LAUNCHES.name) { Text("Launches") }
                    }
                    NavigationBar(modifier = Modifier.fillMaxWidth())

                    // Listen to navigateTo events and navigate
                    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
                    DisposableEffect(lifecycleOwner) {
                        var navigationJob: Job? = null
                        val lifecycle = lifecycleOwner.value.lifecycle
                        val observer = LifecycleEventObserver { owner, event ->
                            if (event == Lifecycle.Event.ON_RESUME) {
                                navigationJob = owner.lifecycleScope.launch {
                                    viewModel.navigateTo.collect {
                                        navController.navigate(it.name)
                                    }
                                }
                            }
                            if (event == Lifecycle.Event.ON_PAUSE) {
                                navigationJob?.cancel()
                                navigationJob = null
                            }
                        }

                        lifecycle.addObserver(observer)
                        onDispose {
                            lifecycle.removeObserver(observer)
                        }
                    }
                }
            }
        }
    }
}

internal class MainViewModel(
    storeCurrentScreen: StoreCurrentScreenUseCase,
    observeCurrentScreen: ObserveCurrentScreenUseCase,
) : ViewModel() {

    val navigateTo: MutableSharedFlow<Screen> = MutableSharedFlow()

    init {
        storeCurrentScreen(DAILY)
        viewModelScope.launch {
            observeCurrentScreen().collect {
                navigateTo.emit(it)
            }
        }
    }
}

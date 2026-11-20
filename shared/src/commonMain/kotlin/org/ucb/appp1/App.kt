package org.ucb.appp1

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.dsl.koinConfiguration
import org.ucb.appp1.di.sharedModule
import org.ucb.appp1.homeconfig.presentation.screen.HomeConfigScreen

@Composable
@Preview
fun App(checkpoint: (@Composable () -> Unit)? = null) {
    KoinApplication(
        configuration = koinConfiguration {
            modules(sharedModule())
        }
    ) {
        MaterialTheme {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                Box(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
                    if (checkpoint != null) checkpoint() else HomeConfigScreen()
                }
            }
        }
    }
}

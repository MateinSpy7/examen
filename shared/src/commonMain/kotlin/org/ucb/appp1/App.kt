package org.ucb.appp1

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
            var showCheckpoint by remember { mutableStateOf(false) }
            Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
                if (checkpoint != null) {
                    TextButton(onClick = { showCheckpoint = !showCheckpoint }) {
                        Text(if (showCheckpoint) "Volver al inicio" else "Abrir Checkpoint")
                    }
                }
                Box(modifier = Modifier.weight(1f)) {
                    if (showCheckpoint && checkpoint != null) checkpoint() else HomeConfigScreen()
                }
            }
        }
    }
}

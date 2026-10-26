package org.ucb.appp1

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.dsl.koinConfiguration
import org.ucb.appp1.di.sharedModule
import org.ucb.appp1.exchange.presentation.screen.ExchangeScreen
import org.ucb.appp1.notes.presentation.screen.NotesScreen
import org.ucb.appp1.notifications.presentation.screen.NotificationsScreen
import org.ucb.appp1.signin.presentation.screen.LoginScreen
import org.ucb.appp1.userinformation.presentation.screen.UserInformationScreen

@Composable
@Preview
fun App() {
    KoinApplication(
        configuration = koinConfiguration {
            modules(sharedModule())
        }
    ) {
        MaterialTheme {
            Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
                Box(modifier = Modifier.weight(1f)) { ExchangeScreen() }
                Box { NotificationsScreen() }
                Box(modifier = Modifier.weight(2f)) { NotesScreen() }
            }
        }
    }
}

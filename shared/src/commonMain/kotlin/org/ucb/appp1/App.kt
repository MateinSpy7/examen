package org.ucb.appp1

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.dsl.koinConfiguration
import org.ucb.appp1.crossref.presentation.screen.CrossrefScreen
import org.ucb.appp1.di.sharedModule
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
            CrossrefScreen()
        }
    }
}

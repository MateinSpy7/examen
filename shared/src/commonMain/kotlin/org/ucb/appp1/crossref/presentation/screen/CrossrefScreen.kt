package org.ucb.appp1.crossref.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.ucb.appp1.crossref.presentation.viewmodel.CrossrefEffect
import org.ucb.appp1.crossref.presentation.viewmodel.CrossrefViewModel

@Composable
fun CrossrefScreen(viewModel: CrossrefViewModel = koinViewModel()) {
    val state = viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when(effect) {
                is CrossrefEffect.ShowToast -> {}
                CrossrefEffect.NavigateToBack -> {}
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Artículos de Crossref",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        when {
            state.value.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            state.value.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Error: ${state.value.error}\nVerifica la conexión a internet del emulador.",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            state.value.articles.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No se encontraron artículos")
                }
            }
            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.value.articles) { article ->
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            Text("Título: ${article.title}", style = MaterialTheme.typography.bodyLarge)
                            Text("Autor(es): ${article.authors.joinToString(", ")}", style = MaterialTheme.typography.bodyMedium)
                            Text("Fecha de publicación: ${article.published}", style = MaterialTheme.typography.bodySmall)
                            Text("Revista: ${article.containerTitle}", style = MaterialTheme.typography.bodySmall)
                            Text("DOI: ${article.doi}", style = MaterialTheme.typography.bodySmall)
                            Text("Tipo: ${article.type}", style = MaterialTheme.typography.bodySmall)
                            Text("URL: ${article.url}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

package org.ucb.appp1.homeconfig.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.ucb.appp1.homeconfig.presentation.viewmodel.HomeConfigViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeConfigScreen(viewModel: HomeConfigViewModel = koinViewModel()) {
    val state = viewModel.state.collectAsState().value

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = { viewModel.refresh() },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (state.isCached) {
                item {
                    Surface(color = MaterialTheme.colorScheme.tertiaryContainer) {
                        Text(
                            text = "Datos cacheados",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            if (!state.hasData) {
                item { Text("Sin datos. Desliza hacia abajo para sincronizar.") }
            }
            if (state.banners.isNotEmpty()) {
                item { Text("Banners", style = MaterialTheme.typography.titleMedium) }
                items(state.banners, key = { "banner-${it.id}" }) { banner ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(banner.title, style = MaterialTheme.typography.titleSmall)
                            Text(banner.subtitle)
                            Text(banner.imageUrl)
                        }
                    }
                }
            }
            if (state.features.isNotEmpty()) {
                item { Text("Features", style = MaterialTheme.typography.titleMedium) }
                items(state.features, key = { "feature-${it.id}" }) { feature ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(feature.title, style = MaterialTheme.typography.titleSmall)
                            Text(feature.description)
                            Text(if (feature.enabled) "Activa" else "Inactiva")
                        }
                    }
                }
            }
        }
    }
}

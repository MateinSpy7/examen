package org.ucb.appp1.checkpoint.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.ucb.appp1.checkpoint.data.datasource.BluetoothScanDataSource
import org.ucb.appp1.checkpoint.domain.model.BluetoothDeviceModel
import org.ucb.appp1.checkpoint.domain.model.CheckpointModel
import org.ucb.appp1.checkpoint.domain.model.NfcTagModel
import org.ucb.appp1.checkpoint.presentation.viewmodel.CheckpointState
import org.ucb.appp1.checkpoint.presentation.viewmodel.CheckpointViewModel

private const val MILLIS_PER_SECOND = 1_000L

@Composable
fun CheckpointScreen(
    onRequestPermissions: () -> Unit,
    formatTime: (Long) -> String,
    viewModel: CheckpointViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CheckpointContent(
        state = state,
        formatTime = formatTime,
        onSimulationChange = viewModel::setSimulationMode,
        onSimulateTap = viewModel::simulateTap,
        onScan = viewModel::scanAround,
        onRequestPermissions = onRequestPermissions,
        onClearHistory = viewModel::clearHistory
    )
}

@Composable
private fun CheckpointContent(
    state: CheckpointState,
    formatTime: (Long) -> String,
    onSimulationChange: (Boolean) -> Unit,
    onSimulateTap: () -> Unit,
    onScan: () -> Unit,
    onRequestPermissions: () -> Unit,
    onClearHistory: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().navigationBarsPadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Header(state.simulationMode, onSimulationChange) }
        item { NfcCard(state, onSimulateTap) }
        item { BluetoothCard(state, onScan, onRequestPermissions) }
        if (state.devices.isNotEmpty()) {
            item { SectionTitle("Dispositivos encontrados (${state.devices.size})") }
            items(state.devices, key = { it.id }) { DeviceRow(it) }
        }
        item { HistoryHeader(hasRecords = state.history.isNotEmpty(), onClear = onClearHistory) }
        if (state.history.isEmpty()) {
            item {
                Text(
                    "Aún no hay registros. Acerca una tarjeta NFC para crear el primero.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        items(state.history, key = { it.timestampMillis }) { HistoryRow(it, formatTime) }
    }
}

@Composable
private fun Header(simulationMode: Boolean, onSimulationChange: (Boolean) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Column {
            Text(
                "Checkpoint",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Acerca una tarjeta NFC y la app buscará los dispositivos Bluetooth a tu alrededor.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Modo simulación", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Genera un toque de tarjeta y dispositivos de ejemplo",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Switch(checked = simulationMode, onCheckedChange = onSimulationChange)
            }
        }
    }
}

@Composable
private fun NfcCard(state: CheckpointState, onSimulateTap: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Tarjeta NFC", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            when {
                state.simulationMode -> Button(
                    onClick = onSimulateTap,
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Simular toque de tarjeta") }
                state.nfcAvailable -> Text("Acerca una tarjeta a la parte trasera del celular.")
                else -> Text("NFC no disponible o desactivado. Actívalo en los ajustes del celular.")
            }
            state.lastTag?.let { TagInfo(it) }
            Text(
                "Solo se lee el ID de la tarjeta. Algunas tarjetas bancarias muestran un ID distinto en cada toque.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun TagInfo(tag: NfcTagModel) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Última tarjeta leída", style = MaterialTheme.typography.labelMedium)
            Text(
                tag.id,
                style = MaterialTheme.typography.titleMedium,
                fontFamily = FontFamily.Monospace
            )
            Text(
                "Tecnologías: ${tag.technologies.joinToString()}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun BluetoothCard(
    state: CheckpointState,
    onScan: () -> Unit,
    onRequestPermissions: () -> Unit
) {
    val canScan = state.simulationMode || state.bluetoothPermission
    val scanSeconds = BluetoothScanDataSource.DEFAULT_SCAN_MILLIS / MILLIS_PER_SECOND
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Bluetooth", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            if (canScan) {
                FilledTonalButton(
                    onClick = onScan,
                    enabled = !state.isScanning,
                    modifier = Modifier.fillMaxWidth()
                ) { Text(if (state.isScanning) "Escaneando…" else "Escanear alrededor") }
                if (state.isScanning) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    Text(
                        "Buscando dispositivos durante $scanSeconds segundos",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            } else {
                Text("Se necesita el permiso de Bluetooth para buscar dispositivos cercanos.")
                Button(onClick = onRequestPermissions, modifier = Modifier.fillMaxWidth()) { Text("Dar permiso") }
                Text(
                    "Si el aviso no aparece, actívalo en Ajustes > Aplicaciones > Permisos.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            state.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
}

@Composable
private fun HistoryHeader(hasRecords: Boolean, onClear: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Historial",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        OutlinedButton(onClick = onClear, enabled = hasRecords) { Text("Borrar historial") }
    }
}

@Preview
@Composable
fun CheckpointPreview() {
    val closest = BluetoothDeviceModel(id = "SIM-01", name = "Audífonos de ejemplo", rssi = -45)
    MaterialTheme {
        CheckpointContent(
            state = CheckpointState(
                simulationMode = true,
                lastTag = NfcTagModel(id = "3F:A0:12:9C", technologies = listOf("IsoDep", "NfcA")),
                devices = listOf(closest, BluetoothDeviceModel(id = "SIM-03", name = null, rssi = -78)),
                history = listOf(CheckpointModel(timestampMillis = 0L, deviceCount = 2, closestDevice = closest))
            ),
            formatTime = { "12:00:00" },
            onSimulationChange = {},
            onSimulateTap = {},
            onScan = {},
            onRequestPermissions = {},
            onClearHistory = {}
        )
    }
}

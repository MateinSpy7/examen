package org.ucb.appp1.checkpoint.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.ucb.appp1.checkpoint.domain.model.BluetoothDeviceModel
import org.ucb.appp1.checkpoint.presentation.viewmodel.CheckpointState
import org.ucb.appp1.checkpoint.presentation.viewmodel.CheckpointViewModel

@Composable
fun CheckpointScreen(
    onRequestPermissions: () -> Unit,
    formatTime: (Long) -> String,
    viewModel: CheckpointViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Checkpoint")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Modo simulacion")
                    Switch(checked = state.simulationMode, onCheckedChange = viewModel::setSimulationMode)
                }
            }
        }
        item { NfcCard(state, onSimulateTap = viewModel::simulateTap) }
        item {
            BluetoothCard(
                state = state,
                onScan = viewModel::scanAround,
                onRequestPermissions = onRequestPermissions
            )
        }
        items(state.devices, key = { it.id }) { device ->
            Text(deviceLabel(device))
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Historial")
                TextButton(onClick = viewModel::clearHistory) { Text("Borrar historial") }
            }
        }
        if (state.history.isEmpty()) {
            item { Text("Aun no hay registros") }
        }
        items(state.history, key = { it.timestampMillis }) { checkpoint ->
            val closest = checkpoint.closestDevice?.let { deviceLabel(it) } ?: "ninguno"
            Text("${formatTime(checkpoint.timestampMillis)} - ${checkpoint.deviceCount} dispositivos - Mas cercano: $closest")
        }
    }
}

@Composable
private fun NfcCard(state: CheckpointState, onSimulateTap: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Tarjeta NFC")
            when {
                state.simulationMode -> Button(onClick = onSimulateTap) { Text("Simular toque de tarjeta") }
                state.nfcAvailable -> Text("Acerca una tarjeta NFC a la parte trasera del celular")
                else -> Text("NFC no disponible o desactivado. Activalo en los ajustes del celular")
            }
            state.lastTag?.let {
                Text("ID: ${it.id}")
                Text("Tecnologias: ${it.technologies.joinToString()}")
            }
            Text("Solo se lee el ID de la tarjeta. Algunas tarjetas bancarias muestran un ID distinto en cada toque")
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
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Bluetooth")
            if (canScan) {
                Button(onClick = onScan, enabled = !state.isScanning) { Text("Escanear alrededor") }
            } else {
                Text("Se necesita el permiso de Bluetooth para escanear")
                Button(onClick = onRequestPermissions) { Text("Dar permiso") }
            }
            if (state.isScanning) Text("Escaneando 5 segundos...")
            state.error?.let { Text("Error: $it") }
        }
    }
}

private fun deviceLabel(device: BluetoothDeviceModel) =
    "${device.name ?: "Sin nombre"} (${device.rssi} dBm)"

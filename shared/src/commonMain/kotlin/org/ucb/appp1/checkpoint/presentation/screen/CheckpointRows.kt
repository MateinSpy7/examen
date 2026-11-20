package org.ucb.appp1.checkpoint.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.ucb.appp1.checkpoint.domain.model.BluetoothDeviceModel
import org.ucb.appp1.checkpoint.domain.model.CheckpointModel

private const val RSSI_MIN = -100
private const val RSSI_RANGE = 60f
private const val RSSI_VERY_NEAR = -55
private const val RSSI_NEAR = -70
private const val SIGNAL_BAR_WIDTH_DP = 72

@Composable
internal fun DeviceRow(device: BluetoothDeviceModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(device.name ?: "Sin nombre", style = MaterialTheme.typography.titleSmall)
                Text(device.id, style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("${device.rssi} dBm", style = MaterialTheme.typography.labelLarge)
                Text(proximityLabel(device.rssi), style = MaterialTheme.typography.bodySmall)
                LinearProgressIndicator(
                    progress = { signalFraction(device.rssi) },
                    modifier = Modifier.width(SIGNAL_BAR_WIDTH_DP.dp)
                )
            }
        }
    }
}

@Composable
internal fun HistoryRow(checkpoint: CheckpointModel, formatTime: (Long) -> String) {
    val closest = checkpoint.closestDevice
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    formatTime(checkpoint.timestampMillis),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text("${checkpoint.deviceCount} dispositivos", style = MaterialTheme.typography.labelLarge)
            }
            Text(
                if (closest == null) {
                    "Más cercano: ninguno"
                } else {
                    "Más cercano: ${closest.name ?: "Sin nombre"} (${closest.rssi} dBm)"
                },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private fun signalFraction(rssi: Int): Float = ((rssi - RSSI_MIN) / RSSI_RANGE).coerceIn(0f, 1f)

private fun proximityLabel(rssi: Int): String = when {
    rssi >= RSSI_VERY_NEAR -> "Muy cerca"
    rssi >= RSSI_NEAR -> "Cerca"
    else -> "Lejos"
}

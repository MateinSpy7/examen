package org.ucb.appp1.checkpoint.presentation.viewmodel

import org.ucb.appp1.checkpoint.domain.model.BluetoothDeviceModel
import org.ucb.appp1.checkpoint.domain.model.CheckpointModel
import org.ucb.appp1.checkpoint.domain.model.NfcTagModel

data class CheckpointState(
    val simulationMode: Boolean = false,
    val bluetoothPermission: Boolean = false,
    val nfcAvailable: Boolean = true,
    val lastTag: NfcTagModel? = null,
    val isScanning: Boolean = false,
    val readingTag: Boolean = false,
    val devices: List<BluetoothDeviceModel> = emptyList(),
    val history: List<CheckpointModel> = emptyList(),
    val error: String? = null
)

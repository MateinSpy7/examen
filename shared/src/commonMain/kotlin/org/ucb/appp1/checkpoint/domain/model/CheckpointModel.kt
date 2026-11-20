package org.ucb.appp1.checkpoint.domain.model

data class CheckpointModel(
    val timestampMillis: Long,
    val deviceCount: Int,
    val closestDevice: BluetoothDeviceModel?
)

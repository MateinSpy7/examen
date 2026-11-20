package org.ucb.appp1.checkpoint.data.datasource

import org.ucb.appp1.checkpoint.domain.model.BluetoothDeviceModel

interface BluetoothScanDataSource {
    suspend fun scan(durationMillis: Long = DEFAULT_SCAN_MILLIS): List<BluetoothDeviceModel>

    companion object {
        const val DEFAULT_SCAN_MILLIS = 5_000L
    }
}

package org.ucb.appp1.checkpoint.data.service

import kotlinx.coroutines.delay
import org.ucb.appp1.checkpoint.data.datasource.BluetoothScanDataSource
import org.ucb.appp1.checkpoint.domain.model.BluetoothDeviceModel

class SimulatedBluetoothScanner : BluetoothScanDataSource {

    override suspend fun scan(durationMillis: Long): List<BluetoothDeviceModel> {
        delay(durationMillis)
        return sampleDevices.sortedByDescending { it.rssi }
    }

    private val sampleDevices = listOf(
        BluetoothDeviceModel(id = "SIM-01", name = "Audifonos de ejemplo", rssi = -45),
        BluetoothDeviceModel(id = "SIM-02", name = "Reloj de ejemplo", rssi = -62),
        BluetoothDeviceModel(id = "SIM-03", name = null, rssi = -78),
        BluetoothDeviceModel(id = "SIM-04", name = "Parlante de ejemplo", rssi = -88)
    )
}

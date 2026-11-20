package org.ucb.appp1.checkpoint.data.service

import com.juul.kable.Scanner
import kotlinx.coroutines.withTimeoutOrNull
import org.ucb.appp1.checkpoint.data.datasource.BluetoothScanDataSource
import org.ucb.appp1.checkpoint.domain.model.BluetoothDeviceModel
import kotlin.time.Duration.Companion.milliseconds

class KableBluetoothScanner : BluetoothScanDataSource {

    override suspend fun scan(durationMillis: Long): List<BluetoothDeviceModel> {
        val found = mutableMapOf<String, BluetoothDeviceModel>()
        withTimeoutOrNull(durationMillis.milliseconds) {
            Scanner().advertisements.collect { advertisement ->
                val id = advertisement.identifier.toString()
                found[id] = BluetoothDeviceModel(
                    id = id,
                    name = advertisement.name ?: found[id]?.name,
                    rssi = advertisement.rssi
                )
            }
        }
        return found.values.sortedByDescending { it.rssi }
    }
}

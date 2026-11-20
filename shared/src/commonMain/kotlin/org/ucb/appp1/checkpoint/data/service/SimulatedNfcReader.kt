package org.ucb.appp1.checkpoint.data.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.ucb.appp1.checkpoint.data.datasource.NfcReaderDataSource
import org.ucb.appp1.checkpoint.domain.model.NfcTagModel
import kotlin.random.Random

class SimulatedNfcReader : NfcReaderDataSource {

    private val taps = MutableSharedFlow<NfcTagModel>(extraBufferCapacity = 1)

    override fun isAvailable(): Boolean = true

    override fun observeTags(): Flow<NfcTagModel> = taps

    // Como en algunas tarjetas bancarias, el ID cambia en cada toque
    fun simulateTap() {
        val id = Random.nextBytes(TAG_ID_BYTES).joinToString(":") {
            it.toUByte().toString(HEX_RADIX).padStart(2, '0').uppercase()
        }
        taps.tryEmit(NfcTagModel(id = id, technologies = listOf("IsoDep", "NfcA")))
    }

    private companion object {
        const val TAG_ID_BYTES = 4
        const val HEX_RADIX = 16
    }
}

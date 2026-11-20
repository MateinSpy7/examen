package org.ucb.appp1.checkpoint.data.datasource

import kotlinx.coroutines.flow.Flow
import org.ucb.appp1.checkpoint.domain.model.NfcTagModel

interface NfcReaderDataSource {
    fun isAvailable(): Boolean
    fun observeTags(): Flow<NfcTagModel>
}

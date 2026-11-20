package org.ucb.appp1.checkpoint.data.service

import android.app.Activity
import android.content.Context
import android.nfc.NfcAdapter
import android.nfc.Tag
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.ucb.appp1.checkpoint.data.datasource.NfcReaderDataSource
import org.ucb.appp1.checkpoint.domain.model.NfcTagModel

class AndroidNfcReader(private val context: Context) : NfcReaderDataSource {

    @Volatile
    var activity: Activity? = null

    override fun isAvailable(): Boolean =
        NfcAdapter.getDefaultAdapter(context)?.isEnabled == true

    override fun observeTags(): Flow<NfcTagModel> = callbackFlow {
        val currentActivity = activity
        val adapter = NfcAdapter.getDefaultAdapter(context)
        if (currentActivity == null || adapter == null) {
            close()
            return@callbackFlow
        }
        adapter.enableReaderMode(
            currentActivity,
            { tag -> trySend(tag.toModel()) },
            READER_FLAGS,
            null
        )
        awaitClose { adapter.disableReaderMode(currentActivity) }
    }

    // Solo se lee el identificador y la lista de tecnologias: nunca se abre conexion con la tarjeta
    private fun Tag.toModel() = NfcTagModel(
        id = id.joinToString(":") { "%02X".format(it) },
        technologies = techList.map { it.substringAfterLast('.') }
    )

    private companion object {
        const val READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A or
            NfcAdapter.FLAG_READER_NFC_B or
            NfcAdapter.FLAG_READER_NFC_F or
            NfcAdapter.FLAG_READER_NFC_V or
            NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK
    }
}

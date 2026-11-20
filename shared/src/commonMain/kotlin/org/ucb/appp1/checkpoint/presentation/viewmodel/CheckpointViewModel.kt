package org.ucb.appp1.checkpoint.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juul.kable.UnmetRequirementException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ucb.appp1.checkpoint.data.datasource.BluetoothScanDataSource
import org.ucb.appp1.checkpoint.data.datasource.NfcReaderDataSource
import org.ucb.appp1.checkpoint.data.service.SimulatedBluetoothScanner
import org.ucb.appp1.checkpoint.data.service.SimulatedNfcReader
import org.ucb.appp1.checkpoint.domain.model.BluetoothDeviceModel
import org.ucb.appp1.checkpoint.domain.model.CheckpointModel
import org.ucb.appp1.checkpoint.domain.model.NfcTagModel
import kotlin.time.Clock

class CheckpointViewModel(
    private val bluetoothScanner: BluetoothScanDataSource,
    private val nfcReader: NfcReaderDataSource,
    private val simulatedScanner: SimulatedBluetoothScanner,
    private val simulatedNfcReader: SimulatedNfcReader
): ViewModel() {

    private val _state = MutableStateFlow(CheckpointState())
    val state = _state.asStateFlow()

    private var nfcJob: Job? = null
    private var nfcActive = false

    fun startNfc() {
        nfcActive = true
        listenNfc()
    }

    fun stopNfc() {
        nfcActive = false
        nfcJob?.cancel()
    }

    fun setBluetoothPermission(granted: Boolean) {
        _state.update { it.copy(bluetoothPermission = granted) }
    }

    fun setSimulationMode(enabled: Boolean) {
        _state.update { it.copy(simulationMode = enabled, lastTag = null, devices = emptyList(), error = null) }
        if (nfcActive) listenNfc()
    }

    fun simulateTap() {
        if (_state.value.simulationMode) simulatedNfcReader.simulateTap()
    }

    fun scanAround() {
        scan(saveToHistory = false)
    }

    fun clearHistory() {
        _state.update { it.copy(history = emptyList()) }
    }

    private fun listenNfc() {
        nfcJob?.cancel()
        val reader = if (_state.value.simulationMode) simulatedNfcReader else nfcReader
        val available = reader.isAvailable()
        _state.update { it.copy(nfcAvailable = available) }
        if (!available) return
        nfcJob = viewModelScope.launch {
            reader.observeTags().collect { onTagTapped(it) }
        }
    }

    private fun onTagTapped(tag: NfcTagModel) {
        _state.update { it.copy(lastTag = tag) }
        scan(saveToHistory = true)
    }

    private fun scan(saveToHistory: Boolean) {
        val current = _state.value
        if (current.isScanning) return
        if (!current.simulationMode && !current.bluetoothPermission) {
            _state.update { it.copy(error = "Falta el permiso de Bluetooth") }
            return
        }
        _state.update { it.copy(isScanning = true, error = null) }
        val scanner = if (current.simulationMode) simulatedScanner else bluetoothScanner
        viewModelScope.launch {
            try {
                val devices = scanner.scan()
                _state.update {
                    it.copy(
                        devices = devices,
                        history = if (saveToHistory) listOf(newCheckpoint(devices)) + it.history else it.history
                    )
                }
            } catch (e: UnmetRequirementException) {
                _state.update { it.copy(error = e.message ?: "No se pudo escanear Bluetooth") }
            } finally {
                _state.update { it.copy(isScanning = false) }
            }
        }
    }

    private fun newCheckpoint(devices: List<BluetoothDeviceModel>) = CheckpointModel(
        timestampMillis = Clock.System.now().toEpochMilliseconds(),
        deviceCount = devices.size,
        closestDevice = devices.firstOrNull()
    )
}

package org.ucb.appp1.checkpoint.presentation.screen

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleResumeEffect
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.ucb.appp1.checkpoint.data.service.AndroidNfcReader
import org.ucb.appp1.checkpoint.presentation.viewmodel.CheckpointViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CheckpointRoute(viewModel: CheckpointViewModel = koinViewModel()) {
    val context = LocalContext.current
    val nfcReader = koinInject<AndroidNfcReader>()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        viewModel.setBluetoothPermission(hasBluetoothPermission(context))
    }

    LaunchedEffect(Unit) {
        viewModel.setBluetoothPermission(hasBluetoothPermission(context))
    }

    LifecycleResumeEffect(Unit) {
        nfcReader.activity = context.findActivity()
        viewModel.startNfc()
        onPauseOrDispose {
            viewModel.stopNfc()
            nfcReader.activity = null
        }
    }

    CheckpointScreen(
        onRequestPermissions = { permissionLauncher.launch(bluetoothPermissions()) },
        formatTime = ::formatTime,
        viewModel = viewModel
    )
}

private fun bluetoothPermissions(): Array<String> =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(Manifest.permission.BLUETOOTH_SCAN)
    } else {
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    }

private fun hasBluetoothPermission(context: Context): Boolean =
    bluetoothPermissions().all {
        context.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
    }

private fun Context.findActivity(): Activity? =
    generateSequence(this) { (it as? ContextWrapper)?.baseContext }
        .filterIsInstance<Activity>()
        .firstOrNull()

private fun formatTime(millis: Long): String =
    SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(millis))

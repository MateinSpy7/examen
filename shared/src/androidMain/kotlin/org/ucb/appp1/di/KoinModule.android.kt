package org.ucb.appp1.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.ucb.appp1.checkpoint.data.datasource.NfcReaderDataSource
import org.ucb.appp1.checkpoint.data.service.AndroidNfcReader

private val androidModule = module {
    single { AndroidNfcReader(androidContext()) }
    single<NfcReaderDataSource> { get<AndroidNfcReader>() }
}

fun initKoinAndroid(context: Context) {
    startKoin{
        androidContext(context)
        androidLogger()
        modules(sharedModule() + androidModule)
    }
}

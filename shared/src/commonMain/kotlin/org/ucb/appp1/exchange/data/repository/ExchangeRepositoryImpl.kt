package org.ucb.appp1.exchange.data.repository

import kotlinx.coroutines.flow.Flow
import org.ucb.appp1.exchange.domain.model.ContactInfo
import org.ucb.appp1.exchange.data.datasource.RealTimeDataBase
import org.ucb.appp1.exchange.data.datasource.RemoteConfigDataSource
import org.ucb.appp1.exchange.domain.repository.ExchangeRepository

class ExchangeRepositoryImpl(
    val realTimeDataBase: RealTimeDataBase,
    val remoteConfig: RemoteConfigDataSource
): ExchangeRepository {
    override suspend fun observe(): Flow<String?> {
       return realTimeDataBase.observeMessage()
    }

    override suspend fun getContactInfo(): ContactInfo {
        return remoteConfig.getContactInfo()
    }
}
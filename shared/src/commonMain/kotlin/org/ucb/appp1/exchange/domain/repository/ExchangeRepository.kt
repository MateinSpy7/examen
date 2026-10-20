package org.ucb.appp1.exchange.domain.repository

import kotlinx.coroutines.flow.Flow
import org.ucb.appp1.exchange.domain.model.ContactInfo

interface ExchangeRepository {
    suspend fun observe(): Flow<String?>
    suspend fun getContactInfo(): ContactInfo
}
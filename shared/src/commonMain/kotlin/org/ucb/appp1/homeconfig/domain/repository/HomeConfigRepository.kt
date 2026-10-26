package org.ucb.appp1.homeconfig.domain.repository

import kotlinx.coroutines.flow.Flow
import org.ucb.appp1.homeconfig.domain.model.HomeConfigModel

interface HomeConfigRepository {
    fun observe(): Flow<HomeConfigModel>
    suspend fun sync(): Boolean
}

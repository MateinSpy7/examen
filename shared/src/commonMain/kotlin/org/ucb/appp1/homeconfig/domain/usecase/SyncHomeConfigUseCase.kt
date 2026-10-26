package org.ucb.appp1.homeconfig.domain.usecase

import org.ucb.appp1.homeconfig.domain.repository.HomeConfigRepository

class SyncHomeConfigUseCase(
    val repository: HomeConfigRepository
) {
    suspend fun invoke(): Boolean {
        return repository.sync()
    }
}

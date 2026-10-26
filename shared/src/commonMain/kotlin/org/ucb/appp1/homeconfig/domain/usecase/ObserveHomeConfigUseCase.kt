package org.ucb.appp1.homeconfig.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.ucb.appp1.homeconfig.domain.model.HomeConfigModel
import org.ucb.appp1.homeconfig.domain.repository.HomeConfigRepository

class ObserveHomeConfigUseCase(
    val repository: HomeConfigRepository
) {
    fun invoke(): Flow<HomeConfigModel> {
        return repository.observe()
    }
}

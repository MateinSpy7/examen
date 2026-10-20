package org.ucb.appp1.exchange.domain.usecase

import org.ucb.appp1.exchange.domain.model.ContactInfo
import org.ucb.appp1.exchange.domain.repository.ExchangeRepository

class GetContactInfoUseCase(
    val repository: ExchangeRepository
) {
    suspend fun invoke(): ContactInfo {
        return repository.getContactInfo()
    }
}

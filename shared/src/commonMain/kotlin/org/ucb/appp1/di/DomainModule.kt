package org.ucb.appp1.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.ucb.appp1.crossref.domain.usecase.SearchArticlesUseCase
import org.ucb.appp1.exchange.domain.usecase.GetContactInfoUseCase
import org.ucb.appp1.exchange.domain.usecase.ObserveExchangeUseCase
import org.ucb.appp1.notes.domain.usecase.AddNoteUseCase
import org.ucb.appp1.notes.domain.usecase.DeleteNoteUseCase
import org.ucb.appp1.notes.domain.usecase.ObserveNotesUseCase
import org.ucb.appp1.signin.domain.usecase.AuthenticateUseCase
import org.ucb.appp1.userinformation.domain.usecase.FindAliasUseCase

val domainModule = module {
    singleOf(::AuthenticateUseCase)
    singleOf(::FindAliasUseCase)
    singleOf(::SearchArticlesUseCase)
    singleOf(::ObserveExchangeUseCase)
    singleOf(::GetContactInfoUseCase)
    singleOf(::ObserveNotesUseCase)
    singleOf(::AddNoteUseCase)
    singleOf(::DeleteNoteUseCase)
}

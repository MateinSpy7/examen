package org.ucb.appp1.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.ucb.appp1.crossref.data.datasource.CrossrefRemoteDataSource
import org.ucb.appp1.crossref.data.repository.CrossrefRepositoryImpl
import org.ucb.appp1.crossref.data.service.CrossrefApiService
import org.ucb.appp1.crossref.domain.repository.CrossrefRepository
import org.ucb.appp1.exchange.data.datasource.RealTimeDataBase
import org.ucb.appp1.exchange.data.datasource.RemoteConfigDataSource
import org.ucb.appp1.exchange.data.repository.ExchangeRepositoryImpl
import org.ucb.appp1.exchange.domain.repository.ExchangeRepository
import org.ucb.appp1.notes.data.datasource.FirestoreDataSource
import org.ucb.appp1.notes.data.repository.NoteRepositoryImpl
import org.ucb.appp1.notes.domain.repository.NoteRepository
import org.ucb.appp1.userinformation.data.datasource.GithubRemoteDataSource
import org.ucb.appp1.userinformation.data.repository.GithubRepositoryImpl
import org.ucb.appp1.userinformation.data.service.GitHubApiService
import org.ucb.appp1.userinformation.domain.repository.GithubRepository

val dataModule = module {
    single<GithubRemoteDataSource> { GitHubApiService() }
    single<GithubRepository>{GithubRepositoryImpl(get())}
    single<CrossrefRemoteDataSource> { CrossrefApiService() }
    single<CrossrefRepository>{CrossrefRepositoryImpl(get())}
    singleOf(::RealTimeDataBase)
    singleOf(::RemoteConfigDataSource)
    single<ExchangeRepository> { ExchangeRepositoryImpl(get(), get()) }
    singleOf(::FirestoreDataSource)
    single<NoteRepository> { NoteRepositoryImpl(get()) }
}

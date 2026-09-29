package org.ucb.appp1.crossref.data.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.ucb.appp1.crossref.data.datasource.CrossrefRemoteDataSource
import org.ucb.appp1.crossref.data.dto.CrossrefResponseDto

class CrossrefApiService: CrossrefRemoteDataSource {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30000L
            connectTimeoutMillis = 30000L
            socketTimeoutMillis = 30000L
        }
        defaultRequest {
            header("User-Agent", "AppP1/1.0 (mailto:contacto@ucb.edu.bo)")
        }
    }
    override suspend fun getWorks(query: String, rows: Int): CrossrefResponseDto {
        val response = client.get("https://api.crossref.org/works") {
            parameter("query", query)
            parameter("rows", rows)
        }
        try {
            return response.body<CrossrefResponseDto>()
        } catch (e: Exception) {
            throw e
        }
    }
}

package org.ucb.appp1.homeconfig.data.repository

import dev.gitlive.firebase.FirebaseException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.ucb.appp1.homeconfig.data.datasource.HomeConfigRemoteDataSource
import org.ucb.appp1.homeconfig.data.dto.HomeConfigDto
import org.ucb.appp1.homeconfig.data.local.HomeConfigDao
import org.ucb.appp1.homeconfig.data.local.SyncStatusEntity
import org.ucb.appp1.homeconfig.data.mapper.toEntity
import org.ucb.appp1.homeconfig.data.mapper.toModel
import org.ucb.appp1.homeconfig.domain.model.HomeConfigModel
import org.ucb.appp1.homeconfig.domain.repository.HomeConfigRepository
import kotlin.time.Clock

class HomeConfigRepositoryImpl(
    val remote: HomeConfigRemoteDataSource,
    val dao: HomeConfigDao
): HomeConfigRepository {

    private val json = Json { ignoreUnknownKeys = true }

    override fun observe(): Flow<HomeConfigModel> {
        return combine(
            dao.observeFeatures(),
            dao.observeBanners(),
            dao.observeStatus()
        ) { features, banners, status ->
            HomeConfigModel(
                features = features.map { it.toModel() },
                banners = banners.map { it.toModel() },
                isCached = status?.lastSyncSuccess != true,
                lastAttemptAt = status?.lastAttemptAt ?: 0L
            )
        }
    }

    override suspend fun sync(): Boolean {
        return try {
            val raw = remote.getHomeConfigJson()
            require(raw.isNotBlank()) { "Remote Config no tiene el parametro home_config" }
            val dto = json.decodeFromString<HomeConfigDto>(raw)
            dao.replaceAll(
                features = dto.features.map { it.toEntity() },
                banners = dto.banners.map { it.toEntity() },
                status = SyncStatusEntity(lastAttemptAt = now(), lastSyncSuccess = true)
            )
            true
        } catch (e: FirebaseException) {
            saveFailure(e.message)
        } catch (e: SerializationException) {
            saveFailure(e.message)
        } catch (e: IllegalArgumentException) {
            saveFailure(e.message)
        }
    }

    private suspend fun saveFailure(message: String?): Boolean {
        dao.saveStatus(
            SyncStatusEntity(lastAttemptAt = now(), lastSyncSuccess = false, errorMessage = message)
        )
        return false
    }

    private fun now(): Long = Clock.System.now().toEpochMilliseconds()
}

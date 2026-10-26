package org.ucb.appp1.homeconfig.data.datasource

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.remoteconfig.get
import dev.gitlive.firebase.remoteconfig.remoteConfig
import kotlin.time.Duration.Companion.seconds

class HomeConfigRemoteDataSource {

    private val remoteConfig by lazy { Firebase.remoteConfig }

    suspend fun getHomeConfigJson(): String {
        remoteConfig.settings { minimumFetchInterval = 0.seconds }
        remoteConfig.setDefaults(CONFIG_KEY to "")
        remoteConfig.fetchAndActivate()
        return remoteConfig[CONFIG_KEY]
    }

    private companion object {
        const val CONFIG_KEY = "home_config"
    }
}

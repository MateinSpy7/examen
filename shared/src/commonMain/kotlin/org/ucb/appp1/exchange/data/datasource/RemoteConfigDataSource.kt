package org.ucb.appp1.exchange.data.datasource

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.remoteconfig.get
import dev.gitlive.firebase.remoteconfig.remoteConfig
import org.ucb.appp1.exchange.domain.model.ContactInfo
import kotlin.time.Duration.Companion.seconds

class RemoteConfigDataSource {

    private val remoteConfig by lazy { Firebase.remoteConfig }

    suspend fun getContactInfo(): ContactInfo {
        remoteConfig.settings { minimumFetchInterval = 0.seconds }
        remoteConfig.setDefaults(
            PHONE_KEY to DEFAULT_PHONE,
            EMAIL_KEY to DEFAULT_EMAIL
        )
        remoteConfig.fetchAndActivate()
        return ContactInfo(
            phone = remoteConfig[PHONE_KEY],
            email = remoteConfig[EMAIL_KEY]
        )
    }

    private companion object {
        const val PHONE_KEY = "contact_phone"
        const val EMAIL_KEY = "contact_email"
        const val DEFAULT_PHONE = "Sin telefono (valor por defecto)"
        const val DEFAULT_EMAIL = "Sin email (valor por defecto)"
    }
}

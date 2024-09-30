package com.semonemo.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.semonemo.domain.model.JwtToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenDataSource
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) {
        suspend fun saveJwtToken(jwtToken: JwtToken) {
            dataStore.edit { prefs ->
                prefs[ACCESS_TOKEN_KEY] = jwtToken.accessToken
                prefs[REFRESH_TOKEN_KEY] = jwtToken.refreshToken
            }
        }

        suspend fun getAccessToken(): String? =
            dataStore.data
                .map { prefs ->
                    prefs[ACCESS_TOKEN_KEY]
                }.first()

        suspend fun getRefreshToken(): String? =
            dataStore.data
                .map { prefs ->
                    prefs[REFRESH_TOKEN_KEY]
                }.first()

        suspend fun getJwtToken(): Pair<String, String> =
            dataStore.data.run {
                Pair(getAccessToken() ?: "", getRefreshToken() ?: "")
            }

        suspend fun deleteJwtToken() {
            dataStore.edit { prefs ->
                prefs.remove(ACCESS_TOKEN_KEY)
                prefs.remove(REFRESH_TOKEN_KEY)
            }
        }

        companion object {
            val ACCESS_TOKEN_KEY = stringPreferencesKey("ACCESS_TOKEN_KEY")
            val REFRESH_TOKEN_KEY = stringPreferencesKey("REFRESH_TOKEN_KEY")
        }
    }

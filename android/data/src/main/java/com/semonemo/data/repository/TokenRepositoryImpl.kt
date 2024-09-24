package com.semonemo.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.semonemo.domain.repository.TokenRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokenRepositoryImpl
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) : TokenRepository {
        override suspend fun saveJwtToken(
            accessToken: String,
            refreshToken: String,
        ) {
            dataStore.edit { prefs ->
                prefs[ACCESS_TOKEN_KEY] = accessToken
                prefs[REFRESH_TOKEN_KEY] = refreshToken
            }
        }

        override suspend fun getAccessToken(): String? =
            dataStore.data
                .map { prefs ->
                    prefs[ACCESS_TOKEN_KEY]
                }.first()

        override suspend fun getRefreshToken(): String? =
            dataStore.data
                .map { prefs ->
                    prefs[REFRESH_TOKEN_KEY]
                }.first()

        override suspend fun getJwtToken(): Pair<String, String> =
            dataStore.data.run {
                Pair(getAccessToken() ?: "", getRefreshToken() ?: "")
            }

        override suspend fun saveWalletAddress(address: String) {
            dataStore.edit { prefs ->
                prefs[WALLET_ADDRESS_KEY] = address
            }
        }

        override suspend fun getWalletAddress(): String? =
            dataStore.data
                .map { prefs ->
                    prefs[WALLET_ADDRESS_KEY]
                }.first()

        companion object {
            val ACCESS_TOKEN_KEY = stringPreferencesKey("ACCESS_TOKEN_KEY")
            val REFRESH_TOKEN_KEY = stringPreferencesKey("REFRESH_TOKEN_KEY")
            val WALLET_ADDRESS_KEY = stringPreferencesKey("WALLET_ADDRESS_KEY")
            val NICKNAME_KEY = stringPreferencesKey("NICKNAME_KEY")
            val USER_ID_KEY = stringPreferencesKey("USER_ID_KEY")
            val PROFILE_IMAGE_KEY = stringPreferencesKey("PROFILE_IMAGE_KEY")
        }
    }

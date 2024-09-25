package com.semonemo.data.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthDataSource
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
    ) {
        suspend fun saveWalletAddress(address: String) {
            dataStore.edit { prefs ->
                prefs[WALLET_ADDRESS_KEY] = address
            }
        }

        suspend fun getWalletAddress(): String? =
            dataStore.data
                .map { prefs ->
                    prefs[WALLET_ADDRESS_KEY]
                }.first()

        suspend fun savePassword(password: String) {
            dataStore.edit { prefs ->
                prefs[PASSWORD_KEY] = password
            }
        }

        suspend fun getPassword(): String? =
            dataStore.data
                .map { prefs ->
                    prefs[PASSWORD_KEY]
                }.first()

        companion object {
            val WALLET_ADDRESS_KEY = stringPreferencesKey("WALLET_ADDRESS_KEY")
            val PASSWORD_KEY = stringPreferencesKey("PASSWORD_KEY")
            val NICKNAME_KEY = stringPreferencesKey("NICKNAME_KEY")
            val USER_ID_KEY = stringPreferencesKey("USER_ID_KEY")
            val PROFILE_IMAGE_KEY = stringPreferencesKey("PROFILE_IMAGE_KEY")
        }
    }

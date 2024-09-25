package com.semonemo.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.semonemo.data.R
import com.semonemo.data.datasource.AuthDataSource
import com.semonemo.data.datasource.TokenDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Singleton
    @Provides
    fun providesDataStore(
        @ApplicationContext appContext: Context,
    ): DataStore<Preferences> =
        PreferenceDataStoreFactory.create {
            appContext.preferencesDataStoreFile(appContext.getString(R.string.app_name))
        }

    @Singleton
    @Provides
    fun provideTokenDataSource(dataStore: DataStore<Preferences>): TokenDataSource = TokenDataSource(dataStore)

    @Singleton
    @Provides
    fun provideAuthDataSource(dataStore: DataStore<Preferences>): AuthDataSource = AuthDataSource(dataStore)
}

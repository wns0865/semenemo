package com.semonemo.data.di

import com.semonemo.data.repository.AuthRepositoryImpl
import com.semonemo.data.repository.NFTRepositoryImpl
import com.semonemo.data.repository.TokenRepositoryImpl
import com.semonemo.domain.repository.AuthRepository
import com.semonemo.domain.repository.NFTRepository
import com.semonemo.domain.repository.TokenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Singleton
    @Binds
    fun provideNFTRepository(nftRepositoryImpl: NFTRepositoryImpl): NFTRepository

    @Singleton
    @Binds
    fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Singleton
    @Binds
    fun provideTokenRepository(tokenRepositoryImpl: TokenRepositoryImpl): TokenRepository
}

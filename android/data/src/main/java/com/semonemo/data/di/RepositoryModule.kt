package com.semonemo.data.di

import com.semonemo.data.repository.AuthRepositoryImpl
import com.semonemo.data.repository.NFTRepositoryImpl
import com.semonemo.domain.repository.AuthRepository
import com.semonemo.domain.repository.NFTRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun provideNFTRepository(nftRepositoryImpl: NFTRepositoryImpl): NFTRepository

    @Binds
    fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
}

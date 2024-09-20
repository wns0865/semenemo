package com.semonemo.data.di

import com.semonemo.data.repository.NFTRepositoryImpl
import com.semonemo.domain.repository.NFTRepository
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
}

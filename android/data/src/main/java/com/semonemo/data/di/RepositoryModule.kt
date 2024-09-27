package com.semonemo.data.di

import com.semonemo.data.repository.AiRepositoryImpl
import com.semonemo.data.repository.AuthRepositoryImpl
import com.semonemo.data.repository.NFTRepositoryImpl
import com.semonemo.data.repository.UserRepositoryImpl
import com.semonemo.domain.repository.AiRepository
import com.semonemo.domain.repository.AuthRepository
import com.semonemo.domain.repository.NFTRepository
import com.semonemo.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindNFTRepository(nftRepositoryImpl: NFTRepositoryImpl): NFTRepository

    @Binds
    fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    fun bindApiRepository(aiRepositoryImpl: AiRepositoryImpl): AiRepository

    @Binds
    fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}

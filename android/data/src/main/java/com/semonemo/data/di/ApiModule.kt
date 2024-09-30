package com.semonemo.data.di

import com.semonemo.data.network.api.AiApi
import com.semonemo.data.network.api.AuthApi
import com.semonemo.data.network.api.NFTApi
import com.semonemo.data.network.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideNFTApi(
        @NetworkModule.NftUploadClient retrofit: Retrofit,
    ): NFTApi = retrofit.create()

    @Provides
    @Singleton
    fun provideAuthApi(
        @NetworkModule.AuthClient retrofit: Retrofit,
    ): AuthApi = retrofit.create()

    @Provides
    @Singleton
    fun provideAiApi(
        @NetworkModule.AiClient retrofit: Retrofit,
    ): AiApi = retrofit.create()

    @Provides
    @Singleton
    fun provideUserApi(
        @NetworkModule.BaseClient retrofit: Retrofit,
    ): UserApi = retrofit.create()
}

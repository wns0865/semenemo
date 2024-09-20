package com.semonemo.data.di

import com.semonemo.data.network.api.NFTApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideNFTApi(
        @Named("Node") retrofit: Retrofit,
    ): NFTApi = retrofit.create()
}

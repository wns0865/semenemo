package com.semonemo.data.di

import com.google.gson.GsonBuilder
import com.semonemo.data.BuildConfig
import com.semonemo.data.datasource.TokenDataSource
import com.semonemo.data.network.api.AuthApi
import com.semonemo.data.network.interceptor.AccessTokenInterceptor
import com.semonemo.data.network.interceptor.ErrorHandlingInterceptor
import com.semonemo.data.network.interceptor.JwtAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BaseClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class NftClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AiClient

    @Singleton
    @Provides
    @BaseClient
    fun provideOkHttpClient(
        accessTokenInterceptor: AccessTokenInterceptor,
        jwtAuthenticator: JwtAuthenticator,
    ) = OkHttpClient.Builder().run {
        addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        addInterceptor(ErrorHandlingInterceptor())
        addInterceptor(accessTokenInterceptor)
        authenticator(jwtAuthenticator)
        connectTimeout(10, TimeUnit.SECONDS)
        readTimeout(10, TimeUnit.SECONDS)
        writeTimeout(10, TimeUnit.SECONDS)
        build()
    }

    @AuthClient
    @Singleton
    @Provides
    fun provideAuthClient(accessTokenInterceptor: AccessTokenInterceptor) =
        OkHttpClient.Builder().run {
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            addInterceptor(accessTokenInterceptor)
            addInterceptor(ErrorHandlingInterceptor())
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(10, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)
            build()
        }

    @NftClient
    @Singleton
    @Provides
    fun provideNftOkHttpClient(
        accessTokenInterceptor: AccessTokenInterceptor,
        jwtAuthenticator: JwtAuthenticator,
    ) = OkHttpClient.Builder().run {
        addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        addInterceptor(ErrorHandlingInterceptor())
        addInterceptor(accessTokenInterceptor)
        authenticator(jwtAuthenticator)
        connectTimeout(10, TimeUnit.SECONDS)
        readTimeout(10, TimeUnit.SECONDS)
        writeTimeout(10, TimeUnit.SECONDS)
        build()
    }

    @AiClient
    @Singleton
    @Provides
    fun provideAiOkHttpClient() =
        OkHttpClient.Builder().run {
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            addInterceptor(ErrorHandlingInterceptor())
            connectTimeout(5, TimeUnit.MINUTES)
            readTimeout(5, TimeUnit.MINUTES)
            writeTimeout(5, TimeUnit.MINUTES)
            build()
        }

    @Singleton
    @Provides
    @AuthClient
    fun provideAuthRetrofit(
        @AuthClient
        okHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .baseUrl(BuildConfig.SEVER_URL)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    @BaseClient
    fun provideRetrofit(
        @BaseClient
        okHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .baseUrl(BuildConfig.SEVER_URL)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    @NftClient
    fun provideNFTRetrofit(
        @NftClient
        okHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .baseUrl(BuildConfig.TEST_URL + BuildConfig.NODE_PORT_NUMBER)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    @AiClient
    fun provideAiRetrofit(
        @AiClient
        okHttpClient: OkHttpClient,
    ): Retrofit =
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .baseUrl(BuildConfig.AI_SERVER_URL)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideJwtAuthenticator(
        tokenDataSource: TokenDataSource,
        authApi: AuthApi,
    ): JwtAuthenticator = JwtAuthenticator(tokenDataSource, authApi)

    @Singleton
    @Provides
    fun provideAccessTokenInterceptor(tokenDataSource: TokenDataSource): AccessTokenInterceptor = AccessTokenInterceptor(tokenDataSource)
}

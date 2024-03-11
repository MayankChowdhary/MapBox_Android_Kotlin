package com.example.mapbox.di

import android.content.Context
import com.example.mapbox.utils.ApiService
import com.example.mapbox.utils.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    @Singleton
    fun getHttpClient(): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            .build()


    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl("")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()


    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)


  /*  @Provides
    @Singleton
    fun provideRepository(apiService: ApiService, context: Context): WikiRepositoryImpl =
        WikiRepositoryImpl(apiService, context)*/

 /*   @Module
    @InstallIn(SingletonComponent::class) // or whatever graph fits your need the best
    interface RepositoryModules {
        @Binds
        fun provideMainRepositoryImpl(repository: WikiRepositoryImpl): WikiRepository
    }*/

    @Provides
    fun provideContext(
        @ApplicationContext context: Context,
    ): Context {
        return context
    }

}
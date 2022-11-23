package com.waslim.storyapp.di

import android.app.Application
import android.content.Context
import com.waslim.storyapp.BuildConfig
import com.waslim.storyapp.model.datastore.DarkModeSettingPreferences
import com.waslim.storyapp.model.datastore.UserTokenPreferences
import com.waslim.storyapp.model.service.local.RemoteKeysDao
import com.waslim.storyapp.model.service.local.StoryDao
import com.waslim.storyapp.model.local.StoryDatabase
import com.waslim.storyapp.model.service.network.ApiService
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
object AppsModule {

    private const val URL = BuildConfig.BASE_URL

    private val loggingInterceptor = when {
        BuildConfig.DEBUG -> HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        else -> HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
    }

    private val client = OkHttpClient
        .Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit() : Retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    @Singleton
    @Provides
    fun providesApiService(retrofit: Retrofit) : ApiService =
        retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun providesStoryDatabase(context: Application) : StoryDatabase =
        StoryDatabase.getDatabase(context)

    @Singleton
    @Provides
    fun providesStoryDao(storyDatabase: StoryDatabase) : StoryDao =
        storyDatabase.storyDao()

    @Singleton
    @Provides
    fun providesRemoteKeyDao(storyDatabase: StoryDatabase) : RemoteKeysDao =
        storyDatabase.remoteKeysDao()

    @Singleton
    @Provides
    fun providesUserToken(@ApplicationContext context: Context) : UserTokenPreferences =
        UserTokenPreferences(context)

    @Singleton
    @Provides
    fun providesDarkMode(@ApplicationContext context: Context) : DarkModeSettingPreferences =
        DarkModeSettingPreferences(context)
}
package com.example.elviris.api

import android.content.Context
import android.content.SharedPreferences
import com.example.elviris.common.Constants
import com.example.elviris.di.MyApp
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    @Named("url")
    fun provideBaseUrl(): String = Constants.TMDBAPI_BASE_URL

    val logging = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    @Singleton
    @Provides
    fun provideOkHttpClient(tokenInterceptor: TokenInterceptor): OkHttpClient {
        return with(OkHttpClient.Builder()) {
            addInterceptor(tokenInterceptor)
            addInterceptor(logging)
            connectTimeout(Constants.TIMEOUT_INMILIS, TimeUnit.MILLISECONDS)
            build()
        }
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(): SharedPreferences {
        val sharedPref = MyApp.instance?.getSharedPreferences(
            Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE)
        return sharedPref
    }

    @Singleton
    @Provides
    fun provideLoginRetrofitService(@Named("url") baseUrl: String, okHttpClient: OkHttpClient): ElvirisService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ElvirisService::class.java)
    }


}
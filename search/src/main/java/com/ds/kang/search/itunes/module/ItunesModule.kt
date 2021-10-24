package com.ds.kang.search.itunes.module

import com.ds.kang.search.BASE_URL
import com.ds.kang.search.itunes.api.ItunesApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal class ItunesModule {
    fun provideItunesModule(okHttpClient: OkHttpClient?) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(HttpUrl.parse(BASE_URL))
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(getGson()))
            .build()
    }

    private fun getGson() : Gson? {
        return GsonBuilder().create()
    }

    fun provideItunesApi(retrofit: Retrofit) : ItunesApi {
        return retrofit.create(ItunesApi::class.java)
    }
}
package com.example.marvelapp.service.request.generator

import com.example.marvelapp.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MarvelRequestGenerator {
    private val httpClient = OkHttpClient.Builder().addInterceptor { chain ->
        val defaultRequest = chain.request()
        val defaultHttpUrl = defaultRequest.url()
        val httpUrl = defaultHttpUrl.newBuilder()
            .addQueryParameter(TS, TS_VALUE)
            .addQueryParameter(API, BuildConfig.API_VALUE)
            .addQueryParameter(HASH, BuildConfig.HASH_VALUE)
            .build()
        val requestBuilder = defaultRequest.newBuilder().url(httpUrl)
        chain.proceed(requestBuilder.build())
    }

    private val builder = Retrofit.Builder()
        .baseUrl(BuildConfig.MARVEL_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())

    fun <S> createService(serviceClass: Class<S>): S {
        val retrofit = builder.client(httpClient.build()).build()
        return retrofit.create(serviceClass)
    }

    companion object {
        private const val TS = "ts"
        private const val API = "apikey"
        private const val HASH = "hash"
        private const val TS_VALUE = "1"
    }
}

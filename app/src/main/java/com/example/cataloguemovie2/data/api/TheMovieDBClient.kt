/*
 * Copyright (c) 2020.
 * By Risal Fajar Amiyardi
 * risalfajar@gmail.com | github.com/risalfajar
 */

package com.example.cataloguemovie2.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val API_KEY = "ba923ef570e2fda6a523a4ff8aaa7fac"
const val BASE_URL = "https://api.themoviedb.org/3/"
const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500"
const val FIRST_PAGE = 1
const val POST_PER_PAGE = 20

//object is static, doesn't need to be instantiated
object TheMovieDBClient {
    fun getClient(): TheMovieDBInterface{
        val requestInterceptor = Interceptor{
            //interceptor take only one argument which is a lambda function, so parenthesis can be omitted

            val url = it.request()
                .url()
                .newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()

            val request = it.request()
                .newBuilder()
                .url(url)
                .build()

            //explicitly return a value from with @ annotation.
            //lambda always return the value of the last expression implicitly
            return@Interceptor it.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TheMovieDBInterface::class.java)
    }
}
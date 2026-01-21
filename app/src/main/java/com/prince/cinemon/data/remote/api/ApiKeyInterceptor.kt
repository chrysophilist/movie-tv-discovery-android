package com.prince.cinemon.data.remote.api

import com.prince.cinemon.domain.util.ApiKeyProvider
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(
    private val apiKeyProvider: ApiKeyProvider
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()

        val apiKey = runBlocking {
            apiKeyProvider.getApiKey()
        }

        // If API key missing → let request fail gracefully
        if (apiKey.isNullOrBlank()){
            return chain.proceed(originalRequest)
        }

        val newUrl = originalRequest.url.newBuilder()
            .addQueryParameter("apiKey", apiKey)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}
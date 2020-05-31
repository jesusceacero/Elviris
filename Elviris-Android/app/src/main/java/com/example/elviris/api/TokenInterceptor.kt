package com.example.elviris.api

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor() : Interceptor {
    var token:String?=""
    override fun intercept(chain: Interceptor.Chain): Response {

        val original = chain.request()

        if (original.url.encodedPath.contains("/login")&& original.method.equals("post")
            || (original.url.encodedPath.contains("/register") && original.method.equals("post"))
        ) {
            return  chain.proceed(original)
        }

        val originalHttpUrl = original.url
        val requestBuilder = original.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .url(originalHttpUrl)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
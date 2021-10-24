package com.ds.kang.search

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import okhttp3.*
import java.lang.Exception
import java.lang.RuntimeException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

internal class OKHttpGenerator {
    fun getUnsafeOkHttpClient() : OkHttpClient? {
        return try {
            val trustAllCerts =
                arrayOf<TrustManager>(
                    object: X509TrustManager {
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun checkServerTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return arrayOf()
                        }
                    }
                )

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            val sslSocketFactory = sslContext.socketFactory
            val builder = OkHttpClient.Builder()

            builder.sslSocketFactory(
                sslSocketFactory,
                trustAllCerts[0] as X509TrustManager
            )

            builder.hostnameVerifier { hostname: String?, session: SSLSession? -> true }
                .addInterceptor(SearchInterceptor())
                .connectTimeout(CONNECT_TIMEOUT_SECOND, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT_SECOND, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT_SECOND, TimeUnit.SECONDS).build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private class SearchInterceptor : Interceptor {
        private val TAG: String = SearchInterceptor::class.java.simpleName

        private val KEY_CODE = "code"
        private val KEY_MESSAGE = "message"
        private val KEY_CONTENT = "content"
        override fun intercept(chain: Interceptor.Chain) : Response {
            val response = chain.proceed(chain.request())
            return if (response.isSuccessful) {
                val bodyJson = String(response.body()!!.bytes())
                if (TRACE) {
                    Log.d(TAG, "json : $bodyJson")
                }

                var baseResponse = JsonObject().apply {
                    addProperty(KEY_CODE, response.code())
                    addProperty(KEY_MESSAGE, response.message())
                    add(KEY_CONTENT, Gson().fromJson(bodyJson, JsonElement::class.java))
                }

                val baseResponseString = Gson().toJson(baseResponse)
                if (TRACE) {
                    Log.d(TAG, "baseResponseString : $baseResponseString")
                }

                val contentsType = response.body()!!.contentType()
                response.newBuilder().body(ResponseBody.create(contentsType, baseResponseString))
                    .build()
            } else {
                var baseResponse = JsonObject().apply {
                    addProperty(KEY_CODE, response.code())
                    addProperty(KEY_MESSAGE, response.message())
                }

                val baseResponseString = Gson().toJson(baseResponse)
                val contentsType = response.body()!!.contentType()
                response.newBuilder().body(ResponseBody.create(contentsType, baseResponseString))
                    .build()
            }
        }
    }
}
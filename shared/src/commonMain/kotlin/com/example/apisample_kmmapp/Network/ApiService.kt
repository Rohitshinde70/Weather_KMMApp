package com.example.apisample_kmmapp.Network

import com.example.apisample_kmmapp.Model.RequestModel
import com.example.apisample_kmmapp.Model.ResponseModel
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*


interface ApiService {

    suspend fun getCurrentWeather(request: RequestModel): ResponseModel

    companion object {


        fun create(): ApiService {

            val json = kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = false


            }

            val client = HttpClient {
                // Logging
                install(Logging) {
                    level = LogLevel.ALL
                }
                // JSON
                install(JsonFeature) {
                    serializer = KotlinxSerializer(json)
                }
                // Timeout
                install(HttpTimeout) {
                    requestTimeoutMillis = 15000L
                    connectTimeoutMillis = 15000L
                   socketTimeoutMillis = 15000L
                }
                // Apply to all requests
                defaultRequest {
                    if (method != HttpMethod.Get) contentType(ContentType.Application.Json)
                    accept(ContentType.Application.Json)
                }
            }

            return object : ApiService {
                override suspend fun getCurrentWeather(request: RequestModel): ResponseModel {
                    return client.get("${ApiRoutes.BASE_URL}${ApiRoutes.FORECAST}") {
                        parameter("q", request.q)
                        parameter("key", request.key)
                    }
                }
            }

        }
    }
}

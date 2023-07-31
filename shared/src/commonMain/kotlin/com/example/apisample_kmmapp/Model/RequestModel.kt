package com.example.apisample_kmmapp.Model

import kotlinx.serialization.Serializable

@Serializable
data class RequestModel(
    val q: String,
    val key: String
)
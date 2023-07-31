package com.example.apisample_kmmapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
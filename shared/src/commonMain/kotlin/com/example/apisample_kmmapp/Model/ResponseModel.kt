package com.example.apisample_kmmapp.Model

import kotlinx.serialization.Serializable

@Serializable
data class  ResponseModel(
    val location: Location,
    val current: CurrentWeather,
    val forecast: Forecast,
)
    @Serializable
    data class Location(
        val name: String,
        val region: String,
        val country: String,
        val localtime :String,
    )

    @Serializable
    data class CurrentWeather(
        val temp_c: Double,
        val humidity: Int,
    )

@Serializable
data class Forecast(
    val forecastday : List<Forecastday>

)
@Serializable
data class Forecastday(
    val astro : Astro,
)

@Serializable
data class Astro(

    val sunrise : String,
    val sunset : String,
)



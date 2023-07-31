package com.example.apisample_kmmapp.Network


import com.example.apisample_kmmapp.Model.*
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.*


class ApiServiceImpl(private val client: HttpClient) : ApiService {

    override suspend fun getCurrentWeather(request: RequestModel): ResponseModel {
        return try {
            val response = client.get<HttpResponse>("${ApiRoutes.BASE_URL}${ApiRoutes.FORECAST}") {
                parameter("q", request.q)
                parameter("key", request.key)
            }
             
            val responseBody = response.readText()
            println(responseBody) // Print the raw JSON response

            val json = Json.parseToJsonElement(responseBody).jsonObject
            val location = json["location"]?.jsonObject
            val currentWeather = json["current"]?.jsonObject
            val forecast = json["forecast"]?.jsonObject

          println("Location : $location")
           println("Current Weather : $currentWeather")
            println("Forecast : $forecast")


            val locationData = Location(
                name = location?.get("name")?.jsonPrimitive?.content ?: "",
                region = location?.get("region")?.jsonPrimitive?.content ?: "",
                country = location?.get("country")?.jsonPrimitive?.content ?: "",
                localtime = location?.get("localtime")?.jsonPrimitive?.content ?: ""
            )

            val currentWeatherData = CurrentWeather(
                temp_c = currentWeather?.get("temp_c")?.jsonPrimitive?.double ?: 0.0,
                humidity = currentWeather?.get("humidity")?.jsonPrimitive?.int ?: 0
            )

            // Manually extract forecast data from the JSON response
            val forecastData = forecast?.let { forecastJson ->
                val forecastdayArray = forecastJson["forecastday"]?.jsonArray
                forecastdayArray?.mapNotNull { forecastdayElement ->
                    val astroObject = forecastdayElement.jsonObject["astro"]?.jsonObject
                    astroObject?.let { astro ->
                        // Extract the fields for Astro
                        val sunrise = astro["sunrise"]?.jsonPrimitive?.content ?: ""
                        val sunset = astro["sunset"]?.jsonPrimitive?.content ?: ""
                        Astro(sunrise, sunset)
                    }?.let { astro ->
                        Forecastday(astro)
                    }
                }
            } ?: emptyList()




            // Create the ResponseModel using the mapped data
            val responseModel = ResponseModel(location = locationData, current = currentWeatherData,forecast = Forecast(forecastData))
            responseModel


//
//            // Deserialize the extracted fields into your ResponseModel
//            val responseModel = ResponseModel(
//                location = location?.let { Json.decodeFromString(Location.serializer(), it.toString()) }
//                    ?: Location("", "", ""),
//                current = currentWeather?.let { Json.decodeFromString(CurrentWeather.serializer(), it.toString()) }
//                    ?: CurrentWeather(0.0, 0)
//            )
//            responseModel

        } catch (ex: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${ex.response.status.description}")
            throw ex
        } catch (ex: ClientRequestException) {
            // 4xx - responses
            println("Error: ${ex.response.status.description}")
            throw ex
        } catch (ex: ServerResponseException) {
            // 5xx - response
            println("Error: ${ex.response.status.description}")
            throw ex
        }
    }

}


package com.example.apisample_kmmapp.android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apisample_kmmapp.Model.RequestModel
import com.example.apisample_kmmapp.Model.ResponseModel
import com.example.apisample_kmmapp.Network.ApiService
import com.example.apisample_kmmapp.Network.ApiServiceImpl
import io.ktor.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val apiService by lazy {
        ApiService.create()
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface {

                    WeatherScreen()

                }
            }
        }
    }
}

    @Composable
    fun WeatherScreen() {
        val apiService = remember { ApiServiceImpl(HttpClient()) }
        var cityName by remember { mutableStateOf("") }
        var weatherState by remember { mutableStateOf<ResponseModel?>(null) }
        var errorMessage by remember { mutableStateOf("") }

        val coroutineScope = rememberCoroutineScope()

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp),
            color = MaterialTheme.colors.background
        ) {
            Column(
                modifier = Modifier.padding(top = 16.dp),
               // verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    singleLine = true,
                    value = cityName,
                    onValueChange = { newValue ->
                        cityName = newValue
                      //  Log.d("MainActivity", "cityName: $cityName")
                    },
                    label = { Text("Enter city name") }
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        coroutineScope.launch(Dispatchers.IO) {
                            if (cityName.isNotBlank()) {
                                try {
                                    val request = RequestModel(cityName, "5121c3e648f046d09df45242231002")
                                    val weather = apiService.getCurrentWeather(request)
                                    weatherState = weather
                                    errorMessage = "" // Clear the error message if data is available

                                } catch (e: Exception) {
                                    // Handle exception here
                                    errorMessage = "Please enter a correct city name"
                                    e.printStackTrace()
                                }
                            }
                            else {
                                weatherState = null
                                errorMessage = "Please enter a city name"
                            }
                        }
                    },
                   // modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Search")
                }

                Spacer(modifier = Modifier.height(30.dp))

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (errorMessage.isNotBlank()) {
                        Text(errorMessage)
                    } else {
                        weatherState?.let { weather ->
                            WeatherInfo(weather = weather)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

            }
        }
    }

    @Composable
    fun WeatherInfo(weather : ResponseModel) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),

           // verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally


        ) {
            Text(
                style = TextStyle(background = Color.White),
                text = "City : ${weather.location.name}, ${weather.location.country}",
                fontSize = 18.sp,
               fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(11.dp))
            Log.d("Mainactivity" , "City_name: ${weather.location.name}")
            Text(
                style = TextStyle(background = Color.White),
                text = "Region : ${weather.location.region}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Log.d("Mainactivity" , "City_Region: ${weather.location.region}")
            Spacer(modifier = Modifier.height(11.dp))
            Text(
                style = TextStyle(background = Color.White),
                text = "Temperature : ${weather.current.temp_c} °C",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Log.d("MainActivity", "Temperature: ${weather.current.temp_c}")

            Spacer(modifier = Modifier.height(11.dp))
            Text(
                style = TextStyle(background = Color.White),
                text = "Humidity : ${weather.current.humidity}%",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Log.d("MainActivity", "Humidity: ${weather.current.humidity}")

            Spacer(modifier = Modifier.height(11.dp))
            Text(
                style = TextStyle(background = Color.White),
                text = "Date & Time : ${weather.location.localtime} °C",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(11.dp))
            Text(
                style = TextStyle(background = Color.White),
                text = "Sunrise : ${weather.forecast.forecastday[0].astro.sunrise}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Log.d("MainActivity", "Sunrise: ${weather.forecast.forecastday[0].astro.sunrise}")

            Spacer(modifier = Modifier.height(11.dp))
            Text(
                style = TextStyle(background = Color.White),
                text = "Sunset : ${weather.forecast.forecastday[0].astro.sunset}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Log.d("MainActivity", "Sunset: ${weather.forecast.forecastday[0].astro.sunset}")
        }

    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        WeatherScreen()
    }




package com.example.weatherapp.presentation.home_page.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.domain.weather.WeatherData
import com.example.weatherapp.presentation.home_page.WeatherViewModel
import com.example.weatherapp.presentation.theme.CAAA5A5
import com.example.weatherapp.presentation.theme.colorStops
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(
    viewModel: WeatherViewModel, scope: CoroutineScope, drawerState: DrawerState,
) {

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.9f)
            .background(Brush.linearGradient(colorStops = colorStops))
    ) {
        DrawerHeader()
        Spacer(modifier = Modifier.height(32.dp))
        SavedLocationsList(
            scope, drawerState,
            cityWeatherData = viewModel.state.cityWeatherData,
            onCitySelected = { city -> viewModel.onCitySelected(city) }
        )
        FilledTonalButtonExample(onClick = {})
        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
fun DrawerHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 44.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Saved Locations",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.W400,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            Icons.Filled.Search,
            contentDescription = "",
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
    }
}

@Composable
fun SavedLocationsList(
    scope: CoroutineScope,
    drawerState: DrawerState,
    cityWeatherData: Map<String, WeatherData>, // Make sure this is defined correctly
    onCitySelected: (String) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Use the keys from the map to display weather data
        items(cityWeatherData.keys.toList()) { city -> // This should be a String
            val weatherData = cityWeatherData[city]
            if (weatherData != null) {
                LocationCard(
                    scope = scope,
                    drawerState = drawerState,
                    cityName = city,  // city is a String
                    weatherData = weatherData,
                    onCitySelected = { onCitySelected(city) }  // Pass the selected city
                )
            }
        }
    }
}


@Composable
fun LocationCard(
    scope: CoroutineScope,
    drawerState: DrawerState,
    cityName: String,
    weatherData: WeatherData,
    onCitySelected: (String) -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CAAA5A5.copy(0.5f)),
        onClick = {
            onCitySelected(cityName)
            scope.launch {
                drawerState.close()
            }
        },
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = cityName,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W700
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = weatherData.weatherType.weatherDesc,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500
                )
                Spacer(modifier = Modifier.height(22.dp))
                Row {
                    Text(
                        text = "Humidity",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W300
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${weatherData.humidity}%",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W400
                    )
                }
                Row {
                    Text(
                        text = "Wind",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W300
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${weatherData.windSpeed} km/h",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W400
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.End) {
                Spacer(modifier = Modifier.height(20.dp))
                Image(
                    painter = painterResource(id = weatherData.weatherType.iconRes),
                    contentDescription = null,
                    modifier = Modifier.width(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Text(
                        text = "${weatherData.temperatureCelsius}",
                        color = Color.White,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.W500
                    )
                    Text(text = "ºC", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.W500)
                }
            }
        }
    }
}


@Composable
fun FilledTonalButtonExample(onClick: () -> Unit) {
    FilledTonalButton(
        colors = ButtonDefaults.filledTonalButtonColors(containerColor = CAAA5A5.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .blur(45.dp)
            .padding(horizontal = 16.dp, vertical = 32.dp),
        shape = RoundedCornerShape(24.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        onClick = { onClick() }
    ) {
        Icon(Icons.Filled.AddCircle, contentDescription = "", tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Add new", color = Color.White.copy(alpha = 0.8f), fontSize = 24.sp, fontWeight = FontWeight.W500)
    }
}

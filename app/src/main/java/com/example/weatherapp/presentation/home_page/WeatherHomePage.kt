package com.example.weatherapp.presentation.home_page

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.domain.weather.WeatherData
import com.example.weatherapp.presentation.home_page.components.DrawerContent
import com.example.weatherapp.presentation.home_page.components.WeatherDataDisplay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt


@Composable
fun WeatherHomePage(
    viewModel: WeatherViewModel,
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(viewModel, scope, drawerState) },
    ) {
        BackgroundImage(viewModel.state.selectedCity)
        TopBar(
            viewModel,
            scope, drawerState,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            DateSection(
                state = viewModel.state
            )
            WeatherInfo(
                state = viewModel.state
            )
            Spacer(modifier = Modifier.height(32.dp))
            WeatherDataRow(
                state = viewModel.state
            )
            Spacer(modifier = Modifier.height(40.dp))
            WeatherForecastCard(
                state = viewModel.state

            )
        }

        LoadingIndicator(viewModel)
        ErrorMessage(viewModel)
    }
}


@Composable
fun BackgroundImage(city: String) {
    val imageResource = when (city) {
        "London" -> R.drawable.london
        "New York" -> R.drawable.newyork
        "Bejing" -> R.drawable.bejing
        "Tashkent" -> R.drawable.tashkent  // Default city
        else -> R.drawable.tashkent  // Fallback image
    }

    Image(
        painter = painterResource(id = imageResource),
        contentDescription = "Background image for $city",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize(),
    )
}


@Composable
fun TopBar(viewModel: WeatherViewModel, scope: CoroutineScope, drawerState: DrawerState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                if (!viewModel.state.isLoading) {
                    scope.launch {
                        drawerState.apply { if (isClosed) open() else close() }
                    }
                }

            },
        ) {
            Icon(
                Icons.Filled.Menu, contentDescription = "", tint = Color.White, modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(text = viewModel.state.selectedCity, color = Color.White)
        Icon(
            Icons.Filled.LocationOn, contentDescription = "", tint = Color.White,
        )
        Spacer(modifier = Modifier.width(16.dp))
    }
}


@SuppressLint("NewApi")
@Composable
fun DateSection(
    state: WeatherState,
) {
    val today = LocalDate.now()

    val formatter = DateTimeFormatter.ofPattern("MMMM d")
    val formattedDate = today.format(formatter)

    state.weatherInfo?.currentWeatherData?.let { data ->
        Text(
            text = formattedDate, color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.W500
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Updated ${
                data.time.format(
                    DateTimeFormatter.ofPattern("M/d/yyyy h:mm a")
                )
            }",
            color = Color.Black.copy(alpha = 0.5f),
            fontSize = 16.sp,
            fontWeight = FontWeight.W300
        )
    }

}

@Composable
fun WeatherInfo(
    state: WeatherState,

    ) {
    state.weatherInfo?.currentWeatherData?.let { data ->
        Image(
            painter = painterResource(id = data.weatherType.iconRes),
            contentDescription = null,
            modifier = Modifier.width(100.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = data.weatherType.weatherDesc,
            fontSize = 40.sp,
            color = Color.White,
            fontWeight = FontWeight.W500
        )
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "${data.temperatureCelsius}",
                color = Color.White,
                fontSize = 86.sp,
                fontWeight = FontWeight.W500
            )
            Text(
                text = "ºC",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.W700
            )
        }
    }
}

@Composable
fun WeatherDataRow(
    state: WeatherState,
) {
    state.weatherInfo?.currentWeatherData?.let { data ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            WeatherDataDisplay(
                text = "HUMIDITY",
                value = data.humidity.roundToInt(),
                unit = "%",
                icon = ImageVector.vectorResource(id = R.drawable.ic_drop),
            )
            WeatherDataDisplay(
                text = "WIND",
                value = data.windSpeed.roundToInt(),
                unit = "km/h",
                icon = ImageVector.vectorResource(id = R.drawable.ic_wind),
            )
            WeatherDataDisplay(
                text = "FEELS LIKE",
                value = data.temperatureCelsius.roundToInt(),
                unit = "°",
                icon = ImageVector.vectorResource(id = R.drawable.ic_temperature),
            )
        }
    }
}

@Composable
fun WeatherForecastCard(
    state: WeatherState,
) {
    if (state.weatherInfo != null)
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF535353).copy(0.5f)),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                val weatherDataPerDay = state.weatherInfo.weatherDataPerDay

                LazyRow(
                    verticalAlignment = Alignment.CenterVertically,
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    val limitedKeys = weatherDataPerDay.keys.take(4).toList() // Limit to 4 keys

                    items(limitedKeys) { day ->
                        val weatherDataList = weatherDataPerDay[day] ?: emptyList()

                        // Assuming you want to display the first WeatherData for each day
                        val weatherData = weatherDataList.firstOrNull()

                        // Call ForecastItem composable and pass the weatherData if it exists
                        if (weatherData != null) {
                            ForecastItem(weatherData)
                        }
                    }
                }
            }
        }
}


@SuppressLint("NewApi")
@Composable
fun ForecastItem(
    weatherData: WeatherData,
) {
    val formattedTime = remember(weatherData) {
        weatherData.time.format(
            DateTimeFormatter.ofPattern("E d")
        )
    }
    Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = formattedTime,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.W400
        )
        Image(
            painter = painterResource(id = weatherData.weatherType.iconRes),
            contentDescription = null,
            modifier = Modifier.width(40.dp)
        )
        Text(
            text = "${weatherData.temperatureCelsius}°",
            color = Color(0xFFCECECE),
            fontSize = 18.sp,
            fontWeight = FontWeight.W400
        )
        Text(
            text = "${weatherData.windSpeed}\nkm/h",
            color = Color(0xFFCECECE),
            fontSize = 14.sp,
            fontWeight = FontWeight.W400
        )
    }
}

@Composable
fun LoadingIndicator(viewModel: WeatherViewModel) {
    if (viewModel.state.isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    }
}

@Composable
fun ErrorMessage(viewModel: WeatherViewModel) {
    viewModel.state.error?.let { error ->
        Text(
            text = error,
            color = Color.Red,
            textAlign = TextAlign.Center,
        )
    }
}

package com.example.weatherapp.presentation.home_page

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.presentation.home_page.components.DrawerContent
import com.example.weatherapp.presentation.home_page.components.WeatherDataDisplay
import com.example.weatherapp.presentation.theme.Black
import com.example.weatherapp.presentation.theme.C535353
import com.example.weatherapp.presentation.theme.CECECEC
import com.example.weatherapp.presentation.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun WeatherHomePage(
    viewModel: WeatherViewModel,
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(scope, drawerState) },
    ) {
        BackgroundImage()
        TopBar(scope, drawerState)
        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            Text(
                text = "June 07", color = White, fontSize = 40.sp, fontWeight = FontWeight.W500

            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Updated 6/7/2023 6:55 PM",
                color = Black.copy(alpha = 0.5f),
                fontSize = 16.sp,
                fontWeight = FontWeight.W300

            )
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_very_cloudy),
                contentDescription = null,
                modifier = Modifier.width(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Clear", fontSize = 40.sp, color = Color.White, fontWeight = FontWeight.W500
            )
            Row(
                verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "40", color = Color.White, fontSize = 86.sp, fontWeight = FontWeight.W500
                )
                Text(text = "ºC", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.W700)
            }

            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
            ) {

                WeatherDataDisplay(
                    text = "HUMIDITY",
                    value = 24,
                    unit = "%",
                    icon = ImageVector.vectorResource(id = R.drawable.ic_drop),
                )
                WeatherDataDisplay(
                    text = "WIND",
                    value = 9,
                    unit = "km/h",
                    icon = ImageVector.vectorResource(id = R.drawable.ic_wind),
                )
                WeatherDataDisplay(
                    text = "FEELS LIKE",
                    value = 24,
                    unit = "°",
                    icon = ImageVector.vectorResource(id = R.drawable.ic_temperature),
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Card(

                colors = CardDefaults.cardColors(containerColor = C535353.copy(0.5f)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp),


                ) {
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
                ) {
                    LazyRow(
                        verticalAlignment = Alignment.CenterVertically,
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),

                        ) {

                        items(4) {
                            Column(
                                modifier = Modifier.fillMaxHeight(),

                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text(
                                    text = "Wed 16", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.W400
                                )
                                Image(
                                    painter = painterResource(id = R.drawable.ic_snowy),
                                    contentDescription = null,
                                    modifier = Modifier.width(40.dp)
                                )
                                Text(
                                    text = "22º", color = CECECEC, fontSize = 18.sp, fontWeight = FontWeight.W400
                                )
                                Text(
                                    text = "1-5\n" + "km/h",
                                    color = CECECEC,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.W400
                                )
                            }
                        }
                    }
                }
            }

        }
        if (viewModel.state.isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    color = White,
                )
            }
        }
//        viewModel.state.error?.let { error ->
//            Text(
//                text = error,
//                color = Color.Red,
//                textAlign = TextAlign.Center,
//            )
//        }
    }
}


@Composable
fun BackgroundImage() {
    Image(
        painter = painterResource(id = R.drawable.minsk),
        contentDescription = "Background image",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
fun TopBar(scope: CoroutineScope, drawerState: DrawerState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                scope.launch {
                    drawerState.apply { if (isClosed) open() else close() }
                }
            },
        ) {
            Icon(
                Icons.Filled.Menu, contentDescription = "", tint = Color.White, modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "Minsk", color = Color.White)
        Icon(
            Icons.Filled.LocationOn, contentDescription = "", tint = Color.White,
        )
        Spacer(modifier = Modifier.width(16.dp))
    }
}

package com.example.weatherapp

import DefaultLocationTracker
import RetrofitInstance
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.weatherapp.data.repository.WeatherRepositoryImpl
import com.example.weatherapp.presentation.home_page.WeatherHomePage
import com.example.weatherapp.presentation.home_page.WeatherViewModel
import com.example.weatherapp.presentation.theme.WeatherAppTheme
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.ExperimentalCoroutinesApi


class MainActivity : ComponentActivity() {
//    private val viewModel: WeatherViewModel by viewModels()
// private lateinit var permissionLauncher: permissionLauncherActivityResultLauncher<Array<String>>

    @OptIn(ExperimentalCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        permissionLauncher = registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions()
//        ) {
//            viewModel.loadWeatherInfo()
//        }
//        permissionLauncher.launch(arrayOf(
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//        ))
        enableEdgeToEdge()
        window.statusBarColor = Color.Transparent.toArgb()
        window.navigationBarColor = Color.Transparent.toArgb()
        installSplashScreen()
        setContent {
            WeatherAppTheme {
                val api = RetrofitInstance.homeApi
//                val locationTrackerService = DefaultLocationTracker(
//                    locationClient = LocationServices.getFusedLocationProviderClient(this),
//                    application = application,
//                )
                val repo = WeatherRepositoryImpl(api = api)
                val viewModel = WeatherViewModel(
                    repository = repo,
//                    locationTracker = locationTrackerService
                )

                WeatherHomePage(
                    viewModel = viewModel,
                )
            }
        }
    }


}

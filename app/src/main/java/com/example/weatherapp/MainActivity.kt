package com.example.weatherapp

import DefaultLocationTracker
import RetrofitInstance
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.*
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
    private lateinit var viewModel: WeatherViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        window.statusBarColor = Color.Transparent.toArgb()
        window.navigationBarColor = Color.Transparent.toArgb()
        installSplashScreen()

        // Create dependencies
        val api = RetrofitInstance.homeApi
        val locationTrackerService = DefaultLocationTracker(
            locationClient = LocationServices.getFusedLocationProviderClient(application),
            application = application,
        )
        val repo = WeatherRepositoryImpl(api = api)

        // Initialize ViewModel
        viewModel = WeatherViewModel(
            repository = repo,
            locationTracker = locationTrackerService
        )

        // Request location permissions
        registerForActivityResult(RequestMultiplePermissions()) { permissions ->
            if (permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                viewModel.loadWeatherInfo()  // Call to load weather info if permissions are granted
                viewModel.loadWeatherForCities()
            } else {
                viewModel.loadWeatherForCities()
                // Handle the case where permissions are denied (e.g., show a message)
            }
        }.launch(arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
        ))

        setContent {
            WeatherAppTheme {
                WeatherHomePage(viewModel = viewModel)
            }
        }
    }
}


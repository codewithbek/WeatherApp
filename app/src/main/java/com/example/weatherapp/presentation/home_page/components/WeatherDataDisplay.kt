package com.example.weatherapp.presentation.home_page.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WeatherDataDisplay(
    value: Int,
    text: String,
    unit: String,

    icon: ImageVector,
    modifier: Modifier = Modifier,
    iconTint: Color = Color.White,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(40.dp)
        )
        Text(
            text = text,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$value$unit",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500        )
    }
}
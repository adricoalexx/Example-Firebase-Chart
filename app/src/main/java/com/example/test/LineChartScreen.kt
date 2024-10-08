package com.example.test

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TemperatureChartScreen(
    viewModel : ViewModel = viewModel()
) {
    val sensorTemps by viewModel.sensorTemps.observeAsState(emptyList())

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (sensorTemps.isNotEmpty()) {
            LineChartView(
                context = LocalContext.current,
                temperatures = sensorTemps
            )
        } else {
            Text(text = "Loading chart data...")
        }
    }
}
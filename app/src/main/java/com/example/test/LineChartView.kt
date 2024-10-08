package com.example.test

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun LineChartView(
    context : Context,
    temperatures : List<Float>
) {
    AndroidView(
        modifier = Modifier
            .height(400.dp)
            .fillMaxWidth(), factory = {
        LineChart(context).apply {
            updateChartData(temperatures)
        }
    },
        update = { chart ->
            chart.updateChartData(temperatures)
        }
    )
}

private fun LineChart.updateChartData(temperatures: List<Float>) {
    val entries = temperatures.mapIndexed { index, temp ->
        Entry(index.toFloat(), temp)
    }
    val dataSet = LineDataSet(entries, "Sensor Temperatures").apply {
        color = android.graphics.Color.BLUE
        valueTextColor = android.graphics.Color.BLACK
        lineWidth = 2f
        setDrawCircles(true)
        setDrawCircleHole(false)
        circleRadius = 5f
        setCircleColor(android.graphics.Color.RED)
        mode = LineDataSet.Mode.CUBIC_BEZIER
    }
    val lineData = LineData(dataSet)
    this.data = lineData
    this.invalidate() // Refresh the chart
}
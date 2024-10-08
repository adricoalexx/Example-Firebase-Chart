package com.example.test

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewModel(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private val database = FirebaseDatabase.getInstance().reference.child("Sensor")

    private val _sensorTemps = MutableLiveData<List<Float>>()
    val sensorTemps : LiveData<List<Float>> get() = _sensorTemps

    init {
        fetchSensorData()
    }

    private fun fetchSensorData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val sensorList = mutableListOf<Float>()

                for (sensorSnapshot in snapshot.children) {
                    val sensor = sensorSnapshot.getValue(Sensor::class.java)

                    // Log sensor for debugging
                    Log.d("SensorData", "Retrieved sensor: $sensor")

                    // Skip entries with null or invalid values
                    if (sensor != null && sensor.temp != null && sensor.temp.isNotEmpty()) {
                        sensor.temp.replace("'", "").toFloatOrNull()?.let {
                            sensorList.add(it) // Add valid temperature to the list
                        } ?: run {
                            Log.e("SensorData", "Invalid temperature format for sensor: $sensor")
                        }
                    } else {
                        Log.e("SensorData", "Sensor is null or temperature is missing: $sensor")
                    }
                }

                // Log the collected temperatures for debugging
                Log.d("SensorData", "Updated temperatures: $sensorList")

                // Only update LiveData if there are six distinct temperatures
                if (sensorList.size == 6 && sensorList.distinct().size == sensorList.size) {
                    _sensorTemps.value = sensorList
                } else {
                    Log.e("SensorData", "Unexpected number of temperatures: ${sensorList.size}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error : ", "onCancelled: ${error.message}")
            }
        })
    }

}
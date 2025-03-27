package com.example.motamot

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class CapteurManager  (pContext: EndGameActivity): SensorEventListener {

    private val context = pContext
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)


    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_GYROSCOPE){
            val yRotationRate = event.values[1]
            val xRotationRate = event.values[0]
            if (yRotationRate > 2.0 || xRotationRate >2.0){
                Log.d("axelle", "Mouvement détecté")
                context.motionDetected()
            }
        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }


    fun start() {
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)
        Log.d("axelle", "sensor started")
    }


    fun stop() {
        sensorManager.unregisterListener(this)
        Log.d("axelle", "sensors stopped")
    }
}
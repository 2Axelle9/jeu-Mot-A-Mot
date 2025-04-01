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
            // Si mouvement de l'appareil (inclinaison avant/arrière et gauche/droite) détecté
            if (yRotationRate > 2.0 || xRotationRate >2.0){
                Log.d("axelle", "Mouvement détecté")
                context.motionDetected() // On appelle la fonction motionDetected de l'activité context (endGameActivity)
            }
        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }


    fun start() {
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL) // Lorsque l'activity est lancée détecte le gyroscope
        Log.d("axelle", "sensor started")
    }


    fun stop() {
        sensorManager.unregisterListener(this) // Lorsque l'activity est en pause on arrête de détecter les capteurs
        Log.d("axelle", "sensors stopped")
    }
}
package com.example.smartorder.wearos

import android.app.Activity
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.example.smartorder.R
import com.example.smartorder.wearos.utils.GyroscopeData
import com.example.smartorder.wearos.utils.dataSensors
import com.example.smartorder.wearos.utils.sendMessageToPhone

class UserState : Activity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager

    private lateinit var pressure: TextView
    private lateinit var pulse: TextView
    private lateinit var xGyroscope: TextView
    private lateinit var yGyroscope: TextView
    private lateinit var zGyroscope: TextView

    private var heartRateSensor: Sensor? = null
    private var gyroscopeSensor: Sensor? = null
    private var spo2Sensor: Sensor? = null // si el reloj lo soporta

    private var lastBpm = 0
    private var lastPressure = 0f
    private var lastGyro = GyroscopeData(0f, 0f, 0f)
    private val handler = Handler(Looper.getMainLooper())
    private val sendInterval = 500L // ms
    private val sendRunnable = object : Runnable {
        override fun run() {
            sendMessageToPhone(
                "/sensors",
                dataSensors(
                    lastBpm,
                    lastPressure,
                    lastGyro),
                this@UserState
            )
            handler.postDelayed(this, sendInterval)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_state)

        // Referencias a los TextView
        pressure = findViewById(R.id.spO)
        pulse = findViewById(R.id.pulse)
        xGyroscope = findViewById(R.id.xGyroscope)
        yGyroscope = findViewById(R.id.yGyroscope)
        zGyroscope = findViewById(R.id.zGyroscope)

        // Manager de sensores
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        // Ritmo cardiaco
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)

        // Giroscopio
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        // SpO₂ (oxígeno en sangre) - si el hardware lo soporta
        spo2Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

        if (checkSelfPermission(android.Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.BODY_SENSORS), 1)
        }
    }

    override fun onResume() {
        super.onResume()
        // registrar sensores
        heartRateSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        gyroscopeSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        spo2Sensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }

        // iniciar envío periódico
        handler.post(sendRunnable)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        handler.removeCallbacks(sendRunnable)
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_HEART_RATE -> {
                lastBpm = event.values[0].toInt()
                pulse.text = lastBpm.toString()
            }
            Sensor.TYPE_GYROSCOPE -> {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                lastGyro = GyroscopeData(x, y, z)
                xGyroscope.text = "x: %.2f".format(x)
                yGyroscope.text = "y: %.2f".format(y)
                zGyroscope.text = "z: %.2f".format(z)
            }
            Sensor.TYPE_PRESSURE -> {
                val pressureValue = event.values[0]
                val altitude = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pressureValue)
                lastPressure = altitude
                pressure.text = "%.1f".format(altitude)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
package com.sargis.khlopuzyan.overplay.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sargis.khlopuzyan.overplay.databinding.FragmentOverplayBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class OverplayFragment : DaggerFragment() {

    companion object {
        fun newInstance() = OverplayFragment()
    }

    @Inject
    lateinit var viewModel: OverplayViewModel

    private var sensorManager: SensorManager? = null

    private var rotationVectorSensor: Sensor? = null
    private var gyroscopeEventListener: SensorEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        rotationVectorSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        gyroscopeEventListener = object : SensorEventListener {

            override fun onSensorChanged(event: SensorEvent) {
                viewModel.deviceSensorChanged(event.values)
            }

            override fun onAccuracyChanged(sensor: Sensor?, i: Int) {

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentOverplayBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        lifecycle.addObserver(viewModel)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (gyroscopeEventListener != null && rotationVectorSensor != null) {
            sensorManager?.registerListener(
                gyroscopeEventListener,
                rotationVectorSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onPause() {
        sensorManager?.unregisterListener(gyroscopeEventListener)
        super.onPause()
    }
}
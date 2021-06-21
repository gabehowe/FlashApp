package io.github.gabehowe.flashapp

import android.R.layout
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import io.github.gabehowe.flashapp.databinding.ActivityMainBinding
import java.lang.NumberFormatException
import java.util.*
import java.util.Timer.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private var mCameraManager: CameraManager? = null
    private var mCameraId: String? = null
    lateinit var toggleButton: ToggleButton
    private lateinit var binding: ActivityMainBinding
    var devMode = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        val isFlashAvailable = applicationContext.packageManager
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
        if (!isFlashAvailable) {
            showNoFlashError()
        }
        mCameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        try {
            mCameraId = mCameraManager!!.cameraIdList[0]
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        toggleButton = findViewById(R.id.toggle)
        var flashesSecond: EditText
        var isChecked = false
        toggleButton.setOnCheckedChangeListener { buttonView, currentChecked ->
            if (!currentChecked) {
                switchFlashLight(false)
            }
            isChecked = currentChecked
            flashesSecond = findViewById(R.id.flashAmount)
            var lastTorchMode = true
            thread {
                try {
                    flashesSecond.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    switchFlashLight(currentChecked)
                    return@thread
                }
                while (isChecked) {
                    switchFlashLight(lastTorchMode)
                    Thread.sleep(1000 / (flashesSecond.text.toString().toInt()).toLong())
                    lastTorchMode = lastTorchMode.not()

                }
            }
        }
        (findViewById<Button>(R.id.switchView)).setOnClickListener {
            val switchActivityIntent = Intent(this, MorseActivity::class.java)
            startActivity(switchActivityIntent)
        }
    }

    fun showNoFlashError() {
        val alert: AlertDialog = AlertDialog.Builder(this)
            .create()
        alert.setTitle("Oops!")
        alert.setMessage("Flash not available in this device...")
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
            DialogInterface.OnClickListener { dialog, which -> finish() })
        alert.show()
    }

    fun switchFlashLight(status: Boolean) {
        try {
            mCameraManager!!.setTorchMode(mCameraId!!, status)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }
}
package io.github.gabehowe.flashapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import io.github.gabehowe.flashapp.databinding.ActivityMorseBinding
import kotlin.concurrent.thread

class MorseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMorseBinding
    private var mCameraManager: CameraManager? = null
    private var mCameraId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_morse)
        binding = ActivityMorseBinding.inflate(layoutInflater)
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
        var isOn = false
        val startFlashingButton = findViewById<Button>(R.id.startFlashingButton)
        startFlashingButton.setOnClickListener {
            if (!isOn) {
                isOn = true
                startFlashingButton.text = "STOP"
                thread {
                    val morseString = MorseHandler.translateMorse(findViewById<EditText>(R.id.inputForMorse).text.toString())
                    val flashesArray = MorseHandler.translateFlashes(morseString)
                    print("")
                    for (i in flashesArray) {
                        switchFlashLight(i == 1)
                        Thread.sleep(250)
                    }
                    isOn = false
                    startFlashingButton.text = "START"
                }
            }
            else if (isOn) {
                isOn = false
                startFlashingButton.text = "START"
            }

        }


    }

    fun showNoFlashError() {
        val alert: AlertDialog = AlertDialog.Builder(this)
            .create()
        alert.setTitle("Oops!")
        alert.setMessage("Flash not available in this device...")
        alert.setButton(
            DialogInterface.BUTTON_POSITIVE, "OK",
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
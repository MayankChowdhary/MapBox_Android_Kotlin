package com.example.mapbox.utils

import android.content.Context
import android.os.*
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.mapbox.MapBox
import com.google.android.material.snackbar.Snackbar

@Suppress("DEPRECATION")
class Vibration {
    companion object {
        fun vibrate(millisecond: Long = 1000, message: String? = null) {
            if (millisecond != 0L) {
                val vibrator = MapBox.context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            millisecond,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                } else {
                    vibrator.vibrate(millisecond)
                }
            }

            message?.let {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(MapBox.context, it, Toast.LENGTH_SHORT).show()
                }
                Log.d("Vibrator", it)
            }
        }

        fun vibrateWithSnackbar(millisecond: Long = 1000, message: String? = null, anchor: View) {
            if (millisecond != 0L) {
                val vibrator = MapBox.context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            millisecond,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                } else {
                    vibrator.vibrate(millisecond)
                }
            }

            message?.let {
                Handler(Looper.getMainLooper()).post {
                    Snackbar.make(anchor, message, Snackbar.LENGTH_SHORT).show()
                }
                Log.d("Vibrator", it)
            }

        }
    }


}
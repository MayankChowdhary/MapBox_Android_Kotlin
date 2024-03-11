package com.example.mapbox

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MapBox : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }


    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null

    }

}
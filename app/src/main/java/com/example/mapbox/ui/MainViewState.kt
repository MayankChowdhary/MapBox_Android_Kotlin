package com.example.mapbox.ui

import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class MainViewState : BaseObservable() {

    @Bindable
    val title = "MapBox"

    @Bindable
    var shrimmerVisibility = View.INVISIBLE

    @Bindable
    var micVisibility = View.VISIBLE

    @Bindable
    var micListening = View.GONE

    @Bindable
    var emptyViewVisibility = View.VISIBLE

    @Bindable
    var recyclerViewVisibility = View.GONE

    @Bindable
    var errorMessageVisibility = View.GONE

    @Bindable
    var errorMessageText: String? = null

    var apiInProgress = false
        set(value) {
            field = value
            if (field) {
                shrimmerVisibility = View.VISIBLE
                recyclerViewVisibility = View.VISIBLE
                errorMessageVisibility = View.GONE
                emptyViewVisibility = View.GONE
            } else {
                recyclerViewVisibility = View.VISIBLE
                shrimmerVisibility = View.INVISIBLE
                errorMessageVisibility = View.GONE
                emptyViewVisibility = View.GONE
            }
            notifyChange()
        }

    var emptyDataRequest = true
        set(value) {
            field = value
            if (field) {
                shrimmerVisibility = View.INVISIBLE
                emptyViewVisibility = View.VISIBLE
                recyclerViewVisibility = View.GONE
                errorMessageVisibility = View.GONE
            } else {
                recyclerViewVisibility = View.VISIBLE
                shrimmerVisibility = View.INVISIBLE
                errorMessageVisibility = View.GONE
                emptyViewVisibility = View.GONE
            }
            notifyChange()
        }


    var micListenerState = false
        set(value) {
            field = value
            if (field) {
                micListening = View.VISIBLE
                micVisibility = View.GONE
            } else {
                micListening = View.GONE
                micVisibility = View.VISIBLE
            }
            notifyChange()
        }

    fun setError(errorText: String) {
        errorMessageVisibility = View.VISIBLE
        errorMessageText = errorText
        notifyChange()
    }

}
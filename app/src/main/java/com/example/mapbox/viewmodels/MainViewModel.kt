package com.example.mapbox.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapbox.room.database.getDatabase
import com.example.mapbox.room.entity.WikiRoomEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor( @ApplicationContext appContext: Context) : ViewModel() {

    val wikiClickLiveData = MutableLiveData<String?>()
    var wikiRoomLiveData: LiveData<List<WikiRoomEntity>> = getDatabase(appContext).wikiDao().getAllDataSet()



    fun wikiItemClick(title: String) {
        wikiClickLiveData.postValue(title)
    }



}
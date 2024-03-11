package com.example.mapbox.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WikiRoomEntity(
    @PrimaryKey
    val pageId: Int,
    val title: String?,
    val thumbnail: String?,
    val description: String?,
)

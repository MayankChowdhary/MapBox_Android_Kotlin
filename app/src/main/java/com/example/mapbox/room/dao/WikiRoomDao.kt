package com.example.mapbox.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mapbox.room.entity.WikiRoomEntity


@Dao
interface WikiRoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(wikiRoomEntity: WikiRoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(wikiDataList: List<WikiRoomEntity>)

    @Query("SELECT * FROM WikiRoomEntity")
    fun getAllDataSet(): LiveData<List<WikiRoomEntity>>


    @Query("DELETE FROM WikiRoomEntity WHERE WikiRoomEntity.pageId NOT IN(:wikiRoomIds)")
    fun deleteOldUsers(wikiRoomIds: List<Int>)

    @Query("DELETE FROM WikiRoomEntity")
    fun deleteAll()
}

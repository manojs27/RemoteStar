package com.example.videofeed.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.videofeed.database.db.VideoDetailsModel
import io.reactivex.Flowable

@Dao
interface VideoDetailsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertVideoDetails(videoDetailsModel: VideoDetailsModel)

    @Query("SELECT *  FROM VideoDetailsModel")
    fun getVideoDetails(): Flowable<MutableList<VideoDetailsModel>>

    @Query("DELETE  FROM VideoDetailsModel")
    fun delete()

}
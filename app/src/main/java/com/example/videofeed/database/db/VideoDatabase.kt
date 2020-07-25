package com.example.videofeed.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.videofeed.database.dao.VideoDetailsDao

@Database(entities = [(VideoDetailsModel::class)], version = 1, exportSchema = true)

abstract class VideoDatabase : RoomDatabase() {
    abstract fun applyContentsInfoDetailsDao(): VideoDetailsDao
}
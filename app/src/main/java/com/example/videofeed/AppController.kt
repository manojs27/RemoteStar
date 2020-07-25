package com.example.videofeed

import android.app.Application
import androidx.room.Room
import com.example.videofeed.database.db.VideoDatabase
import com.facebook.stetho.Stetho

class AppController : Application() {
    private val DATABASE_NAME = "remotestar"
    private var database: VideoDatabase? = null

    override fun onCreate() {
        super.onCreate()
        Stetho.initialize(
            Stetho.newInitializerBuilder(this)
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build()
        )
        database =
            Room.databaseBuilder(applicationContext, VideoDatabase::class.java, DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
        INSTANCE = this

    }

    companion object {
        lateinit var INSTANCE: AppController
    }

    fun getDB(): VideoDatabase {
        return database!!
    }

}

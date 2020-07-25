package com.example.videofeed.database

import android.content.Context
import com.example.videofeed.AppController
import com.example.videofeed.database.db.VideoDetailsModel
import io.reactivex.Flowable
import io.reactivex.Observable

class DatabaseRepository(context: Context) {

    fun saveVideosIntoDB(videoDetailsModel: VideoDetailsModel): io.reactivex.Observable<VideoDetailsModel> {
        return Observable.create { subscriber ->
            try {
                AppController.INSTANCE.getDB().applyContentsInfoDetailsDao()
                    .insertVideoDetails(videoDetailsModel)
                subscriber.onNext(videoDetailsModel)
            } catch (e: Exception) {
                subscriber.onError(e)
                e.printStackTrace()
            }
        }
    }

    fun getVideosfromDB(): Flowable<MutableList<VideoDetailsModel>> {
        var activityTypeDetails: Flowable<MutableList<VideoDetailsModel>>? = null

        try {
            activityTypeDetails =
                AppController.INSTANCE.getDB().applyContentsInfoDetailsDao().getVideoDetails()
            if (activityTypeDetails == null) {
            } else {
                activityTypeDetails = activityTypeDetails
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return activityTypeDetails!!
    }

    fun deletefromDB(): io.reactivex.Observable<String> {
        return Observable.create { subscriber ->
            try {
                AppController.INSTANCE.getDB().applyContentsInfoDetailsDao().delete()
                subscriber.onNext("Success")
            } catch (e: Exception) {
                subscriber.onError(e)
                e.printStackTrace()
            }
        }
    }
}
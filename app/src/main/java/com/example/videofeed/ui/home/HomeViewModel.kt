package com.example.videofeed.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.videofeed.database.DatabaseRepository
import com.example.videofeed.database.db.VideoDetailsModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel : ViewModel() {

    private var videoDetailsModel = MutableLiveData<List<VideoDetailsModel>>()

    fun saveVideoInfoDetailsModel(
        videoDetailsModel: VideoDetailsModel,
        databaseRepository: DatabaseRepository?
    ) {
        CompositeDisposable().add(
            databaseRepository!!.saveVideosIntoDB(videoDetailsModel)
                .subscribeOn(Schedulers.computation())
                .subscribe(
                    { result ->
                        Log.d("Insert", "Inserted successfully")
                    },

                    { error ->
                        Log.d("Insert", "Failed to insert")
                    })
        )
    }

    fun getVideoInfoDetailsModel(databaseRepository: DatabaseRepository?) {
        CompositeDisposable().add(
            databaseRepository!!.getVideosfromDB()
                .subscribeOn(Schedulers.computation())
                .subscribe(
                    { result ->
                        if (result.size > 0) {
                            videoDetailsModel.postValue(result)
                        }else{
                            videoDetailsModel.postValue(null)
                        }
                    },

                    { error ->
                        Log.d("Error", "Failed")
                    })
        )
    }

    fun deleteVideosfromDb(databaseRepository: DatabaseRepository?) {
        CompositeDisposable().add(
            databaseRepository!!.deletefromDB()
                .subscribeOn(Schedulers.computation())
                .subscribe(
                    { result ->
                        Log.d("Delete", "Delete Successfully")
                    },

                    { error ->
                        Log.d("Delete", "Failed to Delete")
                    })
        )
    }

    fun videosDataModel(): MutableLiveData<List<VideoDetailsModel>> {
        return videoDetailsModel
    }

}
package com.example.videofeed.model

import android.net.Uri
import java.util.HashMap

class VideoAppModel {

    private val videoMap = HashMap<Uri, Long>()

    fun setVideoSeekTime(uri: Uri, seekTime: Long) {
        videoMap[uri] = seekTime
    }

    fun getSeekTime(uri: Uri): Long {
        return if (videoMap != null && videoMap.containsKey(uri)) {
            videoMap[uri]!!
        } else 0
    }

    companion object {
        val mSharedInstance = VideoAppModel()
    }
}

package com.example.videofeed.model

class VideoData(
    val storageUrl : String,
    val videos : List<Videos>
)

class Videos(
    val description: String,
    val title: String,
    val thumb: String,
    val source: String,
    var subtitle: String
)

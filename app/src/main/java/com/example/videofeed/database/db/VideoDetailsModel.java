package com.example.videofeed.database.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class VideoDetailsModel {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;
    public String title;
    public String subtitle;
    public String thumb;
    public String url;
    public String description;
}

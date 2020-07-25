package com.example.videofeed.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import android.widget.TextView
import com.example.videofeed.R
import com.example.videofeed.database.db.VideoDetailsModel

class HomeViewAdapter(
    private val context: Context,
    private val videos: List<VideoDetailsModel>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<HomeViewAdapter.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home, parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val nameTxt: TextView = holder.textViewName
        val subTxt: TextView = holder.textViewVersion
        val descTxt: TextView = holder.desViewVersion
        val spacecraftImageView: ImageView = holder.imageViewIcon
        nameTxt.setText(videos.get(position).title)
        subTxt.setText(videos.get(position).subtitle)
        descTxt.setText(videos.get(position).description)
        Picasso.get().load(
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/" + videos.get(
                position
            ).thumb
        ).placeholder(R.drawable.ic_launcher_foreground)
            .into(spacecraftImageView)

        spacecraftImageView.setOnClickListener(View.OnClickListener {
            listener.onItemSelected(videos.get(position).url)
        })
    }

    class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewName: TextView
        var textViewVersion: TextView
        var desViewVersion: TextView
        var imageViewIcon: ImageView

        init {
            this.textViewName = itemView.findViewById(R.id.TitleTextView) as TextView
            this.textViewVersion = itemView.findViewById(R.id.SubTitleTextView) as TextView
            this.desViewVersion = itemView.findViewById(R.id.DescTextView) as TextView
            this.imageViewIcon = itemView.findViewById(R.id.HomeImageView) as ImageView
        }
    }

    interface OnItemClickListener {
        fun onItemSelected(videoData: String)
    }
}
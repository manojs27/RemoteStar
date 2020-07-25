package com.example.videofeed.ui.player

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.videofeed.R
import com.example.videofeed.model.VideoAppModel
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultAllocator
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.laserfarm.common.CommonUtils

class VideoPlayerFragment : Fragment(), Player.EventListener {
    companion object {
        private val ARG_URI = "videoUri"

        fun newInstance(videoData: String): VideoPlayerFragment {
            val lBundle = Bundle()
            lBundle.putString(ARG_URI, videoData)
            val fragment = VideoPlayerFragment()
            fragment.setArguments(lBundle)
            return fragment
        }
    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
    }

    override fun onSeekProcessed() {
    }

    override fun onTracksChanged(
        trackGroups: TrackGroupArray?,
        trackSelections: TrackSelectionArray?
    ) {
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
    }

    override fun onLoadingChanged(isLoading: Boolean) {
    }

    override fun onPositionDiscontinuity(reason: Int) {
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
    }

    private var player: SimpleExoPlayer? = null
    private var videoFullScreenPlayer: PlayerView? = null
    private var spinnerVideoDetails: ProgressBar? = null
    private var closeImg: ImageView? = null
    private var videoUri: String? = null
    private var id: String? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (CommonUtils.isOnline(context!!)) {
            setUp()
        } else {
            CommonUtils.ifConnectionNotAvailable(context!!, "Check Internet Connection")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val lView = inflater.inflate(R.layout.fragment_video_player, container, false)
        videoFullScreenPlayer = lView.findViewById(R.id.videoFullScreenPlayer)
        spinnerVideoDetails = lView.findViewById(R.id.spinnerVideoDetails)
        closeImg = lView.findViewById(R.id.closePlayer)

        closeImg!!.setOnClickListener(View.OnClickListener { closeFragment() })
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        if (getArguments() != null) {
            videoUri = getArguments()!!.getString(ARG_URI)
        }
        return lView
    }

    private fun closeFragment() {
        getActivity()!!.supportFragmentManager.popBackStack();
    }

    private fun setUp() {
        initializePlayer()
        if (videoUri == null) {
            return
        }
        buildMediaSource(Uri.parse(videoUri))
    }

    private fun initializePlayer() {
        if (player == null) {
            // 1. Create a default TrackSelector
            val loadControl = DefaultLoadControl(
                DefaultAllocator(true, 16),
                3000, 5000, 1500,
                5000, -1,
                true
            )

            val bandwidthMeter = DefaultBandwidthMeter()
            val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
            val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
            // 2. Create the player
            player = ExoPlayerFactory.newSimpleInstance(
                DefaultRenderersFactory(getActivity()!!.getApplicationContext()),
                trackSelector, loadControl
            )
            videoFullScreenPlayer!!.setPlayer(player)
        }


    }

    private fun buildMediaSource(mUri: Uri) {
        // Measures bandwidth during playback. Can be null if not required.
        val bandwidthMeter = DefaultBandwidthMeter()
        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory = DefaultDataSourceFactory(
            getActivity()!!.getApplicationContext(),
            Util.getUserAgent(
                getActivity()!!.getApplicationContext(),
                getString(R.string.app_name)
            ),
            bandwidthMeter
        )
        // This is the MediaSource representing the media to be played.
        val videoSource = ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mUri)
        // Prepare the player with the source.
        player!!.prepare(videoSource)
        player!!.setPlayWhenReady(true)
        player!!.seekTo(VideoAppModel.mSharedInstance.getSeekTime(mUri))
        player!!.addListener(this)
    }

    override fun onResume() {
        super.onResume()
        resumePlayer()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        VideoAppModel.mSharedInstance.setVideoSeekTime(
            Uri.parse(videoUri),
            player!!.getContentPosition()
        )
        releasePlayer()
    }

    private fun resumePlayer() {
        if (player != null) {
            player!!.setPlayWhenReady(true)
            player!!.getPlaybackState()
        }
    }

    private fun pausePlayer() {
        if (player != null) {
            player!!.setPlayWhenReady(false)
            player!!.getPlaybackState()
        }
    }

    private fun releasePlayer() {
        if (player != null) {
            player!!.release()
            player = null
        }
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        when (playbackState) {
            Player.STATE_BUFFERING -> spinnerVideoDetails!!.visibility = View.VISIBLE
            Player.STATE_ENDED -> {
            }
            Player.STATE_IDLE -> {
            }
            Player.STATE_READY -> spinnerVideoDetails!!.visibility = View.GONE
            else -> {
            }
        }
    }

}

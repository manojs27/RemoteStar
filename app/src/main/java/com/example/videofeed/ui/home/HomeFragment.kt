package com.example.videofeed.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.videofeed.R
import com.example.videofeed.model.VideoData
import com.example.videofeed.model.Videos
import com.example.videofeed.ui.adapter.HomeViewAdapter
import com.example.videofeed.ui.player.VideoPlayerFragment
import com.google.android.material.snackbar.Snackbar
import com.laserfarm.common.CommonUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.videofeed.database.DatabaseRepository
import com.example.videofeed.database.db.VideoDetailsModel
import com.example.videofeed.databinding.FragmentHomeBinding


class HomeFragment : Fragment(), HomeViewAdapter.OnItemClickListener {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    private var adapter: HomeViewAdapter? = null
    private var mRecylcerView: RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    var myProgressBar: ProgressBar? = null
    private var databaseRepository: DatabaseRepository? = null

    private fun populateGridView(videoList: List<VideoDetailsModel>) {
        adapter = HomeViewAdapter(context!!, videoList, this)
        mRecylcerView!!.adapter = adapter
    }

    override fun onItemSelected(videoData: String) {
        Toast.makeText(context, videoData, Toast.LENGTH_SHORT).show()
        activity!!.getSupportFragmentManager().beginTransaction()
            .replace(
                R.id.container,
                VideoPlayerFragment.newInstance("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/" + videoData)
            )
            .addToBackStack(VideoPlayerFragment::class.java!!.getSimpleName())
            .commitAllowingStateLoss()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        binding.viewModel = viewModel
        databaseRepository = DatabaseRepository(activity!!)

        viewModel.getVideoInfoDetailsModel(databaseRepository)

        viewModel.videosDataModel()
            .observe(this, androidx.lifecycle.Observer<List<VideoDetailsModel>> { videosData ->
                if (videosData == null) {
                    callApi()
                } else {
                    populateGridView(videosData)
                }
            })

      //  viewModel.deleteVideosfromDb(databaseRepository)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        mRecylcerView = binding.llRecyclerView
        layoutManager = LinearLayoutManager(context)
        mRecylcerView!!.setLayoutManager(layoutManager)
        mRecylcerView!!.setHasFixedSize(true)
        mRecylcerView!!.setItemAnimator(DefaultItemAnimator())
        myProgressBar = binding.myProgressBar
        return binding.root
    }

    private fun callApi() {
        myProgressBar!!.setIndeterminate(true)
        myProgressBar!!.setVisibility(View.VISIBLE)
        val call = CommonUtils.getSOService(context!!).getVids()
        call.enqueue(object : Callback<VideoData> {

            override fun onResponse(
                call: Call<VideoData>,
                response: Response<VideoData>
            ) {
                myProgressBar!!.setVisibility(View.GONE)
                storeVideoInfo(response.body()?.videos!!)
            }

            override fun onFailure(call: Call<VideoData>, throwable: Throwable) {
                myProgressBar!!.setVisibility(View.GONE)
                Snackbar.make(
                    activity!!.findViewById(R.id.contain),
                    "No Data Found",
                    Snackbar.LENGTH_LONG
                ).setAction("Retry", View.OnClickListener {
                    callApi()
                }).show()
            }
        })
    }

    private fun storeVideoInfo(videoList: List<Videos>) {
        for (videos in videoList) {
            var videoDetailsModel = VideoDetailsModel()
            videoDetailsModel.title = videos.title
            videoDetailsModel.subtitle = videos.subtitle
            videoDetailsModel.description = videos.description
            videoDetailsModel.thumb = videos.thumb
            videoDetailsModel.url = videos.source
            viewModel.saveVideoInfoDetailsModel(videoDetailsModel, databaseRepository)
        }
    }

}
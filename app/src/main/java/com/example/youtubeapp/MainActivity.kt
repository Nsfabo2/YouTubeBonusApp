package com.example.youtubeapp

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

/*
Use the following 3rd party dependency to build a single activity YouTube application with a Recycler View that contains a list of videos:

com.pierfrancescosoffritti.androidyoutubeplayer:core:10.0.5
Perform an internet connection check before trying to display a video
Use dependency documentation to understand how to control videos (Docs)

 - add a listener to gain access to controls (inherit an anonymous object to create an AbstractYouTubePlayerListener)
Each item in the Recycler View should be clickable and play the corresponding video
Recycler View Items should display video names

Edit Android Manifest to require internet connection and network state

Bonus:
Modify theme and colors
Save video id and time stamp to allow continuous play after device rotation
Edit Android Manifest to keep track of rotation
Override onConfigurationChanged to track device rotation
Set video to full screen when in landscape mode, exit full screen in portrait mode
Add a checkbox to each card and use them to display checked videos first (This may be very challenging, can you think of a way to make it work?)
Can you make the app more interesting?
 */

class MainActivity : AppCompatActivity() {
    private val videos: Array<Array<String>> = arrayOf(
        arrayOf("Ancient Egypt", "WqJd8slanIo"),
        arrayOf("Last Person on Earth", "fdCDQIyXGnw"),
        arrayOf("All Trees Were Cut Down", "85yyD-lm4MA"),
        arrayOf("Everyone Froze for 1,000 Years", "k7Y_vNDprCQ"),
        arrayOf("World War III Happened", "DqHctQwqgQ0")
    )
    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var player: YouTubePlayer
    private var currentVideo = 0
    private var timeStamp = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkInternet()

        youTubePlayerView = findViewById(R.id.YoutubePlayer)
        youTubePlayerView.addYouTubePlayerListener(object: AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                player = youTubePlayer
                player.loadVideo(videos[currentVideo][1], timeStamp)
                initializeRV()
            }
        })
title= "Youtube - What if"
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            youTubePlayerView.enterFullScreen()
        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
            youTubePlayerView.exitFullScreen()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("currentVideo", currentVideo)
        outState.putFloat("timeStamp", timeStamp)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        currentVideo = savedInstanceState.getInt("currentVideo", 0)
        timeStamp = savedInstanceState.getFloat("timeStamp", 0f)
    }

    private fun initializeRV(){
        val recyclerView: RecyclerView = findViewById(R.id.VideosRV)
        recyclerView.adapter = VideoRecyclerViewAdapter(videos, player)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
    }

    private fun checkInternet(){
        if(!connectedToInternet()){
            AlertDialog.Builder(this@MainActivity)
                .setTitle("Internet Connection Not Found")
                .setPositiveButton("RETRY"){_, _ -> checkInternet()}
                .show()
        }
    }

    private fun connectedToInternet(): Boolean{
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}//end class
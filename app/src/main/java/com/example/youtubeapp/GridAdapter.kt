package com.example.youtubeapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer

class GridAdapter (private val videoList: Array<Array<String>>,
                   private val player: YouTubePlayer,
                   private val context: Context
): BaseAdapter() {

    private var layoutInflater: LayoutInflater? = null
    lateinit var buttonView: Button

    override fun getCount(): Int {
        return videoList.size
    }

    override fun getItem(position: Int): Any {
        return videoList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        val currentTitle = videoList[position][0]
        val currentLink = videoList[position][1]

        if (layoutInflater==null)
        {
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        }
        if (convertView == null)
        {
            convertView = layoutInflater!!.inflate(R.layout.video_item_row,null)

        }
        if (convertView != null) {
            buttonView = convertView.findViewById(R.id.VideoPlaybtn)
            buttonView.text = currentTitle
            buttonView.setOnClickListener{
                player.loadVideo(currentLink, 0f)
            }
        }
        return convertView
    }
}
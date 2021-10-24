package com.ds.kang.searchtest.ui.track.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ds.kang.search.itunes.obj.TrackInfo
import com.ds.kang.searchtest.R

class TrackListAdapter(
    private val trackInfoList: List<TrackInfo>,
    private val homeTrackPresenter: HomeTrackPresenter
) : RecyclerView.Adapter<TrackListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_row_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(trackInfoList[position])
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.unBind()
    }

    override fun getItemCount() : Int {
        return trackInfoList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val postView: ImageView = itemView.findViewById(R.id.track_poser)
        private val trackNameView: TextView = itemView.findViewById(R.id.track_name)
        private val collectionNameView: TextView = itemView.findViewById(R.id.collection_name)
        private val artistNameView: TextView = itemView.findViewById(R.id.artist_name)
        private val favButton: CheckBox = itemView.findViewById(R.id.fav_checkbox)

        fun bind(trackInfo: TrackInfo) {
            Glide.with(itemView.context)
                .asBitmap()
                .placeholder(android.R.drawable.screen_background_light)
                .load(trackInfo.artworkUrl60)
                .into(postView)

            trackNameView.text = trackInfo.trackName
            collectionNameView.text = trackInfo.collectionName
            artistNameView.text = trackInfo.artistName
            favButton.isChecked = homeTrackPresenter.isFav(trackInfo)
            favButton.setOnCheckedChangeListener { _, isChecked ->
                homeTrackPresenter.toggleFav(!isChecked, trackInfo)
            }
        }

        fun unBind(){
            favButton.setOnCheckedChangeListener(null)
        }
    }
}
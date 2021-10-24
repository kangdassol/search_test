package com.ds.kang.searchtest.ui.track.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ds.kang.searchtest.R
import com.ds.kang.searchtest.dm.favorite.obj.FavTrackInfo
import com.ds.kang.searchtest.ui.track.TrackContract

class FavTrackListAdapter(val favTrackPresenter: FavTrackPresenter)
    : RecyclerView.Adapter<FavTrackListAdapter.ViewHolder>(), TrackContract.FavTrackView {
    private var favTrackInfoList = favTrackPresenter.favTrackInfoList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fav_track_row_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(favTrackInfoList[position])
    }

    override fun getItemCount(): Int {
        return favTrackInfoList.size
    }

    override fun removeFav(position: Int) {
        notifyItemRemoved(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val postView: ImageView = itemView.findViewById(R.id.fav_track_poser)
        private val trackNameView: TextView = itemView.findViewById(R.id.fav_track_name)
        private val collectionNameView: TextView = itemView.findViewById(R.id.fav_collection_name)
        private val artistNameView: TextView = itemView.findViewById(R.id.fav_artist_name)
        private val trashButton: Button = itemView.findViewById(R.id.fav_trash_checkbox)

        fun bind(favTrackInfo: FavTrackInfo) {
            Glide.with(itemView.context)
                .asBitmap()
                .placeholder(android.R.drawable.screen_background_light)
                .load(favTrackInfo.artworkUrl60)
                .into(postView)

            trackNameView.text = favTrackInfo.trackName
            collectionNameView.text = favTrackInfo.collectionName
            artistNameView.text = favTrackInfo.artistName
            trashButton.setOnClickListener {
                favTrackPresenter.toggleFav(true, favTrackInfo)
                removeFav(adapterPosition)
            }
        }
    }
}
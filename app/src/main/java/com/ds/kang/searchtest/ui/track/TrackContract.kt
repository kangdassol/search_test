package com.ds.kang.searchtest.ui.track

import androidx.loader.app.LoaderManager

interface TrackContract {
    interface TrackView<T> {
        fun refreshView(trackInfoList: ArrayList<T>)
    }

    interface FavTrackView {
        fun removeFav(position: Int)
    }

    interface TrackPresenter<T> {
        fun loadFavList(loaderManager: LoaderManager, loadComplete: () -> Unit)
        fun toggleFav(isFav:Boolean, trackInfo: T)
        fun isFav(trackInfo: T) : Boolean
    }
}
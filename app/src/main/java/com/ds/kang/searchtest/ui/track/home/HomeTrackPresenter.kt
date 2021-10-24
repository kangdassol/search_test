package com.ds.kang.searchtest.ui.track.home

import androidx.loader.app.LoaderManager
import com.ds.kang.search.itunes.obj.TrackInfo
import com.ds.kang.searchtest.dm.favorite.FavTrackCallback
import com.ds.kang.searchtest.dm.favorite.FavTrackManager
import com.ds.kang.searchtest.dm.favorite.obj.FavTrackInfo
import com.ds.kang.searchtest.ui.track.TrackContract
import com.ds.kang.searchtest.util.Logger

class HomeTrackPresenter(private val favTrackManager: FavTrackManager<FavTrackInfo>)
    : TrackContract.TrackPresenter<TrackInfo> {

    private val TAG = HomeTrackPresenter::class.simpleName!!
    private var favTrackInfoList:ArrayList<FavTrackInfo> = arrayListOf()

    override fun loadFavList(loaderManager: LoaderManager, loadComplete: () -> Unit){
        favTrackManager.loadList(loaderManager,
            object :FavTrackCallback<MutableList<FavTrackInfo>>{
                override fun onLoadFinished(result: MutableList<FavTrackInfo>?) {
                    favTrackInfoList.clear()
                    favTrackInfoList = ArrayList(result)

                    loadComplete()
                }

                override fun onLoadReset() {

                }
            })
    }

    override fun toggleFav(isFav: Boolean, trackInfo: TrackInfo) {
        if (isFav) {
            val favTrackInfo = getFavTrack(trackInfo)
            favTrackInfo?.let {
                favTrackManager.deleteItem(it)
                favTrackInfoList.remove(it)

                if (Logger.INCLUDE) {
                    Logger.d(TAG, "toggleFav remove, favTrackInfoList : ${favTrackInfoList.size}")
                }
            }
        } else {
            val favTrackInfo = FavTrackInfo.fromTrackInfo(trackInfo)
            favTrackManager.addItem(favTrackInfo)
            favTrackInfoList.add(favTrackInfo)

            if (Logger.INCLUDE) {
                Logger.d(TAG, "toggleFav add, favTrackInfoList : ${favTrackInfoList.size}")
            }
        }
    }

    override fun isFav(trackInfo: TrackInfo) : Boolean {
        return getFavTrack(trackInfo) != null
    }

    private fun getFavTrack(trackInfo: TrackInfo) : FavTrackInfo? {
        for (favTrackInfo in favTrackInfoList) {
            if (trackInfo.trackId == favTrackInfo.trackId) {
                return favTrackInfo
            }
        }

        return null
    }
}
package com.ds.kang.searchtest.ui.track.favorite

import androidx.loader.app.LoaderManager
import com.ds.kang.searchtest.dm.favorite.FavTrackCallback
import com.ds.kang.searchtest.dm.favorite.FavTrackManager
import com.ds.kang.searchtest.dm.favorite.obj.FavTrackInfo
import com.ds.kang.searchtest.ui.track.TrackContract
import com.ds.kang.searchtest.util.Logger

class FavTrackPresenter(private val favTrackManager: FavTrackManager<FavTrackInfo>)
    : TrackContract.TrackPresenter<FavTrackInfo> {

    private val TAG = FavTrackPresenter::class.simpleName!!
    var favTrackInfoList:ArrayList<FavTrackInfo> = arrayListOf()

    override fun loadFavList(loaderManager: LoaderManager, loadComplete: () -> Unit){
        favTrackManager.loadList(loaderManager,
            object : FavTrackCallback<MutableList<FavTrackInfo>> {
                override fun onLoadFinished(result: MutableList<FavTrackInfo>?) {
                    favTrackInfoList.clear()
                    favTrackInfoList = ArrayList(result)

                    loadComplete()
                }

                override fun onLoadReset() {

                }
            })
    }

    override fun toggleFav(isFav: Boolean, favTrackInfo: FavTrackInfo) {
        if (isFav(favTrackInfo)) {
            favTrackInfoList.remove(favTrackInfo)
            favTrackManager.deleteItem(favTrackInfo)

            if (Logger.INCLUDE) {
                Logger.d(TAG, "toggleFav add, favTrackInfoList : ${favTrackInfoList.size}")
            }
        }
    }

    override fun isFav(favTrackInfo: FavTrackInfo) : Boolean {
        return true
    }
}
package com.ds.kang.searchtest.dm.favorite

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.loader.app.LoaderManager
import androidx.loader.content.AsyncTaskLoader
import com.ds.kang.search.itunes.obj.TrackInfo
import com.ds.kang.searchtest.dm.favorite.obj.FavTrackInfo

const val ITEM_ADD: Int = 1
const val ITEM_DELETE: Int = 2

class FavTrackManager<T>(val uri: Uri, val context: Context) {

    fun loadList(loaderManager: LoaderManager,
                 callback: FavTrackCallback<MutableList<FavTrackInfo>>){
        val loader = FavTrackLoader(context, object: FavTrackCallback<MutableList<FavTrackInfo>> {
            override fun onLoadFinished(result: MutableList<FavTrackInfo>?) {
                loaderManager.destroyLoader(FavTrackLoader.LOADER_MAIN)
                Handler(Looper.getMainLooper()).post { callback.onLoadFinished(result) }
            }

            override fun onLoadReset() {
                Handler(Looper.getMainLooper()).post { callback.onLoadReset() }
            }
        })
        loaderManager.restartLoader(FavTrackLoader.LOADER_MAIN, null, loader)
    }

    fun addItem(item: T) {
        addItem(item, object: FavTrackCallback<Long> {
            override fun onLoadFinished(result: Long?) {
            }

            override fun onLoadReset() {
            }
        })
    }

    fun addItem(item: T, callback: FavTrackCallback<Long>) {
        val loader = TrackLoader<T>(context, ITEM_ADD, callback)
        loader.setItem(item)
        loader.forceLoad()
    }

    fun deleteItem(item: T) {
        deleteItem(item, object : FavTrackCallback<Long> {
            override fun onLoadFinished(result: Long?) {
            }

            override fun onLoadReset() {
            }
        })
    }

    fun deleteItem(item: T, callback: FavTrackCallback<Long>) {
        val loader = TrackLoader<T>(context, ITEM_DELETE, callback)
        loader.setItem(item)
        loader.forceLoad()
    }

    inner class TrackLoader<T>(context: Context, private val type: Int, private val callback: FavTrackCallback<Long>) :
        AsyncTaskLoader<Long>(context) {
        private var _item: T? = null

        fun setItem(item: T) {
            _item = item
        }

        override fun loadInBackground(): Long {
            when (type) {
                ITEM_ADD -> {
                    if (_item == null) {
                        return -1
                    }
                    if (_item is FavTrackInfo) {
                        val uri = context.contentResolver.insert(
                            uri,
                            FavTrackInfo.toContentValues(_item as FavTrackInfo)
                        )
                        return ContentUris.parseId(uri!!)
                    } else if (_item is TrackInfo) {
                        val uri = context.contentResolver.insert(
                            uri,
                            FavTrackInfo.toContentValues(_item as TrackInfo)
                        )
                        return ContentUris.parseId(uri!!)
                    }
                }
                ITEM_DELETE -> {
                    if (_item == null) {
                        return -1
                    }

                    if (_item is FavTrackInfo) {
                        val item = _item as FavTrackInfo
                        context.contentResolver.delete(
                            ContentUris.withAppendedId(
                                uri,
                                item.trackId
                            ), null, null
                        )
                        return item.trackId
                    } else if (_item is TrackInfo) {
                        val item = _item as TrackInfo
                        context.contentResolver.delete(
                            ContentUris.withAppendedId(
                                uri,
                                item.trackId
                            ), null, null
                        )

                        return item.trackId
                    }
                }
            }

            return -1
        }

        override fun deliverResult(data: Long?) {
            super.deliverResult(data)
            callback.onLoadFinished(data)
        }
    }
}
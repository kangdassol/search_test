package com.ds.kang.searchtest.dm.favorite

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.ds.kang.searchtest.dm.favorite.TrackConst.COLUMN_ARTIST_NAME
import com.ds.kang.searchtest.dm.favorite.TrackConst.COLUMN_ARTWORK_URL_100
import com.ds.kang.searchtest.dm.favorite.TrackConst.COLUMN_ARTWORK_URL_30
import com.ds.kang.searchtest.dm.favorite.TrackConst.COLUMN_ARTWORK_URL_60
import com.ds.kang.searchtest.dm.favorite.TrackConst.COLUMN_COLLECTION_NAME
import com.ds.kang.searchtest.dm.favorite.TrackConst.COLUMN_CREATE_DATE
import com.ds.kang.searchtest.dm.favorite.TrackConst.COLUMN_TRACK_ID
import com.ds.kang.searchtest.dm.favorite.TrackConst.COLUMN_TRACK_NAME
import com.ds.kang.searchtest.dm.favorite.obj.FavTrackInfo
import com.ds.kang.searchtest.dm.favorite.provider.FavTrackProvider
import java.util.*

internal class FavTrackLoader(
    val context: Context,
    private val favTrackCallback: FavTrackCallback<MutableList<FavTrackInfo>>
) : LoaderManager.LoaderCallbacks<Cursor> {
    override fun onCreateLoader(id: Int, args: Bundle?) : Loader<Cursor> {
        return when (id) {
            LOADER_MAIN -> CursorLoader(
                context.applicationContext,
                FavTrackProvider.URI_ITEM,
                PROTECTION,
                null, null, null
            )
            else -> throw IllegalArgumentException()
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor?>, data: Cursor?) {
        when (loader.id) {
            LOADER_MAIN -> {
                val result: MutableList<FavTrackInfo> = ArrayList<FavTrackInfo>()
                while (data?.moveToNext() == true) {
                    val item: FavTrackInfo = FavTrackInfo.fromCursor(data)
                    result.add(item)
                }
                favTrackCallback.onLoadFinished(result)
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor?>) {
        when (loader.id) {
            LOADER_MAIN -> {
                favTrackCallback.onLoadReset()
            }
        }
    }

    companion object {
        const val LOADER_MAIN = 1
        val PROTECTION = arrayOf(
            COLUMN_TRACK_ID,
            COLUMN_CREATE_DATE,
            COLUMN_TRACK_NAME,
            COLUMN_COLLECTION_NAME,
            COLUMN_ARTIST_NAME,
            COLUMN_ARTWORK_URL_30,
            COLUMN_ARTWORK_URL_60,
            COLUMN_ARTWORK_URL_100
        )
    }
}
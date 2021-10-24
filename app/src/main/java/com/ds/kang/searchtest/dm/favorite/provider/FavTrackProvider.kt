package com.ds.kang.searchtest.dm.favorite.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.ds.kang.searchtest.dm.favorite.TrackConst.FAV_TRACK_TABLE_NAME
import com.ds.kang.searchtest.dm.favorite.obj.FavTrackInfo
import com.ds.kang.searchtest.dm.favorite.room.dao.FavTrackDao
import com.ds.kang.searchtest.dm.favorite.room.database.TrackDatabase

const val AUTHORITY = "com.ds_kang.search.provider"
const val CODE_TRACK_DIR = 1
const val CODE_TRACK_ITEM = 2

class FavTrackProvider : ContentProvider() {
    private val MATCHER: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    private lateinit var favTrackDatabase: TrackDatabase
    private lateinit var favTrackDao: FavTrackDao

    init {
        MATCHER.addURI(AUTHORITY, FAV_TRACK_TABLE_NAME, CODE_TRACK_DIR)
        MATCHER.addURI(AUTHORITY, "$FAV_TRACK_TABLE_NAME/#", CODE_TRACK_ITEM)
    }

    override fun onCreate() : Boolean {
        favTrackDatabase = TrackDatabase.getDatabase(context)
        favTrackDao = favTrackDatabase.favTrackDao()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val code = MATCHER.match(uri)
        return if (code == CODE_TRACK_DIR || code == CODE_TRACK_ITEM) {
            val context = context ?: return null
            val cursor: Cursor? = if (code == CODE_TRACK_DIR) {
                favTrackDao.loadTrackList()
            } else {
                favTrackDao.loadTrack(ContentUris.parseId(uri))
            }
            cursor?.setNotificationUri(context.contentResolver, uri) ?: return null
            return cursor
        } else {
            throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri) : String? {
        return when (MATCHER.match(uri)) {
            CODE_TRACK_DIR -> "vnd.android.cursor.dir/$AUTHORITY.$FAV_TRACK_TABLE_NAME"
            CODE_TRACK_ITEM -> "vnd.android.cursor.item/$AUTHORITY.$FAV_TRACK_TABLE_NAME"
            else -> throw java.lang.IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?) : Uri? {
        return when (MATCHER.match(uri)) {
            CODE_TRACK_DIR -> {
                val id: Long = favTrackDao.insert(FavTrackInfo.fromContentValues(values))
                context!!.contentResolver.notifyChange(uri, null)
                return ContentUris.withAppendedId(uri, id)
            }
            CODE_TRACK_ITEM -> throw java.lang.IllegalArgumentException("Invalid URI, cannot insert as bulk")
            else -> throw java.lang.IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?) : Int {
        return when (MATCHER.match(uri)) {
            CODE_TRACK_DIR -> throw java.lang.IllegalArgumentException("Invalid URI, cannot delete as bulk")
            CODE_TRACK_ITEM -> {
                val context = context ?: return 0
                val count: Int = favTrackDao.deleteById(ContentUris.parseId(uri))
                context.contentResolver.notifyChange(uri, null)
                return count
            }
            else -> throw java.lang.IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?) : Int {
        throw java.lang.IllegalArgumentException("not supported function: $uri")
    }

    companion object {
        val URI_ITEM: Uri = Uri.parse("content://$AUTHORITY/$FAV_TRACK_TABLE_NAME")
    }
}
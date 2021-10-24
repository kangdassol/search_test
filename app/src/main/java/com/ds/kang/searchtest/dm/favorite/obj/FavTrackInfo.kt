package com.ds.kang.searchtest.dm.favorite.obj

import android.content.ContentValues
import android.database.Cursor
import androidx.core.database.getStringOrNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ds.kang.search.itunes.obj.TrackInfo
import com.ds.kang.searchtest.dm.favorite.TrackConst.COLUMN_ARTIST_NAME
import com.ds.kang.searchtest.dm.favorite.TrackConst.COLUMN_ARTWORK_URL_100
import com.ds.kang.searchtest.dm.favorite.TrackConst.COLUMN_ARTWORK_URL_30
import com.ds.kang.searchtest.dm.favorite.TrackConst.COLUMN_ARTWORK_URL_60
import com.ds.kang.searchtest.dm.favorite.TrackConst.COLUMN_COLLECTION_NAME
import com.ds.kang.searchtest.dm.favorite.TrackConst.COLUMN_CREATE_DATE
import com.ds.kang.searchtest.dm.favorite.TrackConst.COLUMN_ID
import com.ds.kang.searchtest.dm.favorite.TrackConst.COLUMN_TRACK_ID
import com.ds.kang.searchtest.dm.favorite.TrackConst.COLUMN_TRACK_NAME
import com.ds.kang.searchtest.dm.favorite.TrackConst.FAV_TRACK_TABLE_NAME

@Entity(tableName = FAV_TRACK_TABLE_NAME)
data class FavTrackInfo(
    @ColumnInfo(name = COLUMN_TRACK_ID)
    val trackId: Long
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    var id: Long = 0

    @ColumnInfo(name = COLUMN_CREATE_DATE)
    var createDate: Long? = null

    @ColumnInfo(name = COLUMN_TRACK_NAME)
    var trackName: String? = null

    @ColumnInfo(name = COLUMN_COLLECTION_NAME)
    var collectionName: String? = null

    @ColumnInfo(name = COLUMN_ARTIST_NAME)
    var artistName: String? = null

    @ColumnInfo(name = COLUMN_ARTWORK_URL_30)
    var artworkUrl30: String? = null

    @ColumnInfo(name = COLUMN_ARTWORK_URL_60)
    var artworkUrl60: String? = null

    @ColumnInfo(name = COLUMN_ARTWORK_URL_100)
    var artworkUrl100: String? = null

    companion object {
        fun toContentValues(trackInfo: TrackInfo): ContentValues {
            return ContentValues().apply {
                put(COLUMN_TRACK_ID, trackInfo.trackId)
                put(COLUMN_CREATE_DATE, System.currentTimeMillis())
                put(COLUMN_TRACK_NAME, trackInfo.trackName)
                put(COLUMN_COLLECTION_NAME, trackInfo.collectionName)
                put(COLUMN_ARTIST_NAME, trackInfo.artistName)
                put(COLUMN_ARTWORK_URL_30, trackInfo.artworkUrl30)
                put(COLUMN_ARTWORK_URL_60, trackInfo.artworkUrl60)
                put(COLUMN_ARTWORK_URL_100, trackInfo.artworkUrl100)
            }
        }

        fun toContentValues(trackInfo: FavTrackInfo): ContentValues {
            return ContentValues().apply {
                put(COLUMN_TRACK_ID, trackInfo.trackId)
                put(COLUMN_CREATE_DATE, trackInfo.createDate)
                put(COLUMN_TRACK_NAME, trackInfo.trackName)
                put(COLUMN_COLLECTION_NAME, trackInfo.collectionName)
                put(COLUMN_ARTIST_NAME, trackInfo.artistName)
                put(COLUMN_ARTWORK_URL_30, trackInfo.artworkUrl30)
                put(COLUMN_ARTWORK_URL_60, trackInfo.artworkUrl60)
                put(COLUMN_ARTWORK_URL_100, trackInfo.artworkUrl100)
            }
        }

        fun fromCursor(cursor: Cursor): FavTrackInfo {
            var trackId: Long? = null
            var createDate: Long? = null
            var trackName: String? = null
            var collectionName: String? = null
            var artistName: String? = null
            var artworkUrl30: String? = null
            var artworkUrl60: String? = null
            var artworkUrl100: String? = null


            if (cursor.getColumnIndex(COLUMN_TRACK_ID) != -1) {
                trackId = cursor.getLong(cursor.getColumnIndex(COLUMN_TRACK_ID))
            }

            if (cursor.getColumnIndex(COLUMN_CREATE_DATE) != -1) {
                createDate = cursor.getLong(cursor.getColumnIndex(COLUMN_CREATE_DATE))
            }

            if (cursor.getColumnIndex(COLUMN_TRACK_NAME) != -1) {
                trackName =
                    cursor.getStringOrNull(cursor.getColumnIndex(COLUMN_TRACK_NAME))
            }

            if (cursor.getColumnIndex(COLUMN_COLLECTION_NAME) != -1) {
                collectionName =
                    cursor.getStringOrNull(cursor.getColumnIndex(COLUMN_COLLECTION_NAME))
            }

            if (cursor.getColumnIndex(COLUMN_ARTIST_NAME) != -1) {
                artistName =
                    cursor.getStringOrNull(cursor.getColumnIndex(COLUMN_ARTIST_NAME))
            }

            if (cursor.getColumnIndex(COLUMN_ARTWORK_URL_30) != -1) {
                artworkUrl30 =
                    cursor.getStringOrNull(cursor.getColumnIndex(COLUMN_ARTWORK_URL_30))
            }

            if (cursor.getColumnIndex(COLUMN_ARTWORK_URL_60) != -1) {
                artworkUrl60 =
                    cursor.getStringOrNull(cursor.getColumnIndex(COLUMN_ARTWORK_URL_60))
            }

            if (cursor.getColumnIndex(COLUMN_ARTWORK_URL_100) != -1) {
                artworkUrl100 =
                    cursor.getStringOrNull(cursor.getColumnIndex(COLUMN_ARTWORK_URL_100))
            }

            return FavTrackInfo(trackId!!).apply {
                this.createDate = createDate ?: 0L
                this.trackName = trackName ?: ""
                this.collectionName = collectionName ?: ""
                this.artistName = artistName ?: ""
                this.artworkUrl30 = artworkUrl30 ?: ""
                this.artworkUrl60 = artworkUrl60 ?: ""
                this.artworkUrl100 = artworkUrl100 ?: ""
            }
        }

        fun fromTrackInfo(trackInfo: TrackInfo): FavTrackInfo {
            return FavTrackInfo(trackInfo.trackId).apply {
                this.createDate = System.currentTimeMillis()
                this.trackName = trackInfo.trackName
                this.collectionName = trackInfo.collectionName
                this.artistName = trackInfo.artistName
                this.artworkUrl30 = trackInfo.artworkUrl30
                this.artworkUrl60 = trackInfo.artworkUrl60
                this.artworkUrl100 = trackInfo.artworkUrl100
            }
        }

        fun fromContentValues(values: ContentValues?): FavTrackInfo {
            var trackId: Long? = null
            var createDate: Long? = null
            var trackName: String? = null
            var collectionName: String? = null
            var artistName: String? = null
            var artworkUrl30: String? = null
            var artworkUrl60: String? = null
            var artworkUrl100: String? = null

            if (values?.containsKey(COLUMN_TRACK_ID) == true) {
                trackId = values.getAsLong(COLUMN_TRACK_ID)
            }

            if (values?.containsKey(COLUMN_CREATE_DATE) == true) {
                createDate = values.getAsLong(COLUMN_CREATE_DATE)
            }

            if (values?.containsKey(COLUMN_TRACK_NAME) == true) {
                trackName = values.getAsString(COLUMN_TRACK_NAME)
            }

            if (values?.containsKey(COLUMN_COLLECTION_NAME) == true) {
                collectionName = values.getAsString(COLUMN_COLLECTION_NAME)
            }

            if (values?.containsKey(COLUMN_ARTIST_NAME) == true) {
                artistName = values.getAsString(COLUMN_ARTIST_NAME)
            }

            if (values?.containsKey(COLUMN_ARTWORK_URL_30) == true) {
                artworkUrl30 = values.getAsString(COLUMN_ARTWORK_URL_30)
            }

            if (values?.containsKey(COLUMN_ARTWORK_URL_60) == true) {
                artworkUrl60 = values.getAsString(COLUMN_ARTWORK_URL_60)
            }

            if (values?.containsKey(COLUMN_ARTWORK_URL_100) == true) {
                artworkUrl100 = values.getAsString(COLUMN_ARTWORK_URL_100)
            }

            return FavTrackInfo(trackId!!).apply {
                this.createDate = createDate ?: 0L
                this.trackName = trackName ?: ""
                this.collectionName = collectionName ?: ""
                this.artistName = artistName ?: ""
                this.artworkUrl30 = artworkUrl30 ?: ""
                this.artworkUrl60 = artworkUrl60 ?: ""
                this.artworkUrl100 = artworkUrl100 ?: ""
            }
        }
    }
}
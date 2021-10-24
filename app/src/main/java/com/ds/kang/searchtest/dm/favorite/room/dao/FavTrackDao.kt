package com.ds.kang.searchtest.dm.favorite.room.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ds.kang.searchtest.dm.favorite.TrackConst.COLUMN_CREATE_DATE
import com.ds.kang.searchtest.dm.favorite.TrackConst.COLUMN_TRACK_ID
import com.ds.kang.searchtest.dm.favorite.TrackConst.FAV_TRACK_TABLE_NAME
import com.ds.kang.searchtest.dm.favorite.obj.FavTrackInfo

@Dao
interface FavTrackDao {
    @Query("SELECT * FROM $FAV_TRACK_TABLE_NAME ORDER BY $COLUMN_CREATE_DATE DESC")
    fun loadTrackList(): Cursor?

    @Query("SELECT * FROM $FAV_TRACK_TABLE_NAME WHERE $COLUMN_TRACK_ID = :id")
    fun loadTrack(id: Long): Cursor?

    @Insert
    fun insert(track: FavTrackInfo): Long

    @Query("DELETE FROM $FAV_TRACK_TABLE_NAME WHERE $COLUMN_TRACK_ID = :id")
    fun deleteById(id: Long): Int

}
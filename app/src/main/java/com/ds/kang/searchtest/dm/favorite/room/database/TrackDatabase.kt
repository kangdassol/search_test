package com.ds.kang.searchtest.dm.favorite.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ds.kang.searchtest.dm.favorite.obj.FavTrackInfo
import com.ds.kang.searchtest.dm.favorite.room.dao.FavTrackDao

@Database(entities = [FavTrackInfo::class], version = 1)
abstract class TrackDatabase : RoomDatabase() {
    abstract fun favTrackDao(): FavTrackDao

    companion object {
        private const val FAV_DATABASE: String = "fav_database"
        @Volatile
        private var INSTANCE: TrackDatabase? = null

        fun getDatabase(context: Context?) : TrackDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context!!.applicationContext,
                    TrackDatabase::class.java,
                    FAV_DATABASE
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
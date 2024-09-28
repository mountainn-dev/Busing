package com.san.busing.data.source.local.provider

import android.content.Context
import androidx.room.Room
import com.san.busing.data.source.local.database.RecentSearchDatabase

object RoomDBProvider {
    private lateinit var db: RecentSearchDatabase

    fun get(context: Context): RecentSearchDatabase {
        if (!::db.isInitialized) {
            db = Room.databaseBuilder(context, RecentSearchDatabase::class.java, "recentSearch").build()
            return db
        } else return db
    }
}
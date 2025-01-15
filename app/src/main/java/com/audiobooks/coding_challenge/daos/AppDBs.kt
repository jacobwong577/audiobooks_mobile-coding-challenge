package com.audiobooks.coding_challenge.daos

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.audiobooks.coding_challenge.entites.PodcastEntity

@Database(entities = [PodcastEntity::class], version = 1)
abstract class AppDBs : RoomDatabase() {
    abstract fun podcastDao(): PodcastDAO

    companion object {
        @Volatile
        private var instance: AppDBs? = null
        fun getInstance(context: Context): AppDBs {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDBs {
            return Room.databaseBuilder(context, AppDBs::class.java, "podcast_db").build()
        }
    }
}
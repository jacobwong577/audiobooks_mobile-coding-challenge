package com.audiobooks.coding_challenge.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.audiobooks.coding_challenge.Constants.DATABASE_PAGE_ITEM_SIZE
import com.audiobooks.coding_challenge.entites.PodcastEntity

@Dao
interface PodcastDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPodcast(podcastEntity: PodcastEntity)

    @Update
    fun updatePodcast(podcastEntity: PodcastEntity)

    @Query("SELECT * FROM PodcastEntity WHERE id = :podcastId LIMIT 1")
    fun findPodcast(podcastId: String): List<PodcastEntity>

    @Query("SELECT * FROM PodcastEntity")
    fun getAllPodcasts(): LiveData<List<PodcastEntity>>

    @Query("SELECT * FROM PodcastEntity LIMIT $DATABASE_PAGE_ITEM_SIZE OFFSET :pageOffset")
    fun getPagePodcasts(pageOffset: Int): LiveData<List<PodcastEntity>>
}

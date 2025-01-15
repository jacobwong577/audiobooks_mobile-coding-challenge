package com.audiobooks.coding_challenge.entites

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class PodcastEntity(
    @PrimaryKey val id: String,
    val title: String,
    val publisher: String,
    val thumbnailUrl: String,
    val description: String,
    var isFavor: Boolean = false
) {
}
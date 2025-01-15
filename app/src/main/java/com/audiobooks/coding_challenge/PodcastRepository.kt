package com.audiobooks.coding_challenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.audiobooks.coding_challenge.daos.PodcastDAO
import com.audiobooks.coding_challenge.entites.PodcastEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class PodcastRepository(private val podcastDAO: PodcastDAO) {
    val dataResult = MutableLiveData<List<PodcastEntity>>()
    val pageResult: LiveData<List<PodcastEntity>> = podcastDAO.getPagePodcasts(0)
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertPodcast(newPodcast: PodcastEntity) {
        coroutineScope.launch(Dispatchers.IO) {
            podcastDAO.insertPodcast(newPodcast)
        }
    }

    fun updatePodcast(podcast: PodcastEntity) {
        coroutineScope.launch(Dispatchers.IO) {
            podcastDAO.updatePodcast(podcast)
        }
    }

    fun findPodcastByPodcastID(podcastId: String) {
        coroutineScope.launch(Dispatchers.Main) {
            dataResult.value = asyncFindPodcastByPodcastId(podcastId).await()
        }
    }

    private fun asyncFindPodcastByPodcastId(podcastId: String): Deferred<List<PodcastEntity>?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async podcastDAO.findPodcast(podcastId)
        }
}
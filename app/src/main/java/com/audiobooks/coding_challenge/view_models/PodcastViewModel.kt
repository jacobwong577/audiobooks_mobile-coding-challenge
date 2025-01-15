package com.audiobooks.coding_challenge.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.audiobooks.coding_challenge.PodcastRepository
import com.audiobooks.coding_challenge.daos.AppDBs
import com.audiobooks.coding_challenge.daos.PodcastDAO
import com.audiobooks.coding_challenge.entites.PodcastEntity
import kotlinx.coroutines.launch

class PodcastViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PodcastRepository
    val dataResults: MutableLiveData<List<PodcastEntity>>
    var pageResults: LiveData<List<PodcastEntity>>
    private val podcastDAO: PodcastDAO

    init {
        podcastDAO = AppDBs.getInstance(application).podcastDao()
        loadFirstSetOfPodcasts()
        repository = PodcastRepository(podcastDAO)
        dataResults = repository.dataResult
        pageResults = repository.pageResult
    }

    fun loadFirstSetOfPodcasts() {
        viewModelScope.launch {
            pageResults = podcastDAO.getPagePodcasts(0)
        }
    }

    fun loadMorePodcasts(listSize: Int) {
        viewModelScope.launch {
            pageResults = podcastDAO.getPagePodcasts(listSize)
        }
    }

    fun findPodcastById(podcastId: String) {
        repository.findPodcastByPodcastID(podcastId)
    }

    fun updatePodcastByObject(podcast: PodcastEntity) {
        repository.updatePodcast(podcast)
    }

}
package com.audiobooks.coding_challenge.screens

import android.os.Bundle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.audiobooks.coding_challenge.Constants.NAV_TO_DETAIL_SCREEN_BUNDLE_KEY_PODCAST_ID
import com.audiobooks.coding_challenge.entites.PodcastEntity
import com.audiobooks.coding_challenge.ui.theme.PurpleGrey40
import com.audiobooks.coding_challenge.view_models.PodcastViewModel

//first page of the items got replaced by the second page of the item after it return from the Detail screen.
// This function is to replace the list after it get a new one from the database
fun <T> SnapshotStateList<T>.newList(newList: List<T>) {
    clear()
    addAll(newList)
}

@Composable
fun ListPodcastScreen(viewModel: PodcastViewModel, navController: NavHostController) {
    //load the first page items
    viewModel.loadFirstSetOfPodcasts()
    val data by viewModel.pageResults!!.observeAsState(listOf())
    Column {
        val displayList = remember { mutableStateListOf<PodcastEntity>() }
        //replace the list with new loaded data
        displayList.newList(data)
        //Screen title
        Text(
            text = "Podcasts",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(10.dp, 10.dp, 0.dp, 0.dp)
        )
        //List view display
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            //use list size to set up each view and use the index to perform paging
            items(displayList.size) {
                ShowEachPodcast(displayList.get(it), navController)
                Divider(
                    color = Color.Gray,
                    thickness = .5.dp,
                    modifier = Modifier.padding(30.dp, 0.dp, 0.dp, 0.dp)
                )
                //try to load more data when it reach the last item of the list.
                if (it == displayList.size - 1) {
                    viewModel.loadMorePodcasts(displayList.size)
                    val data by viewModel.pageResults!!.observeAsState(listOf())
                    if (!data.isEmpty()) {
                        displayList.addAll(data)
                    }
                }
            }
        }
    }
}


@Composable
fun ShowEachPodcast(podcast: PodcastEntity, navController: NavHostController) {
    Row(modifier = Modifier
        .fillMaxSize()
        //Show the Detail screen with the selected Podcast. Use the podcast id to retrieve the data from the database.
        .clickable {
            val id = navController.graph.findNode(Screens.DetailScreen.route)?.id
            if (id != null) {
                val bundle = Bundle()
                bundle.putString(NAV_TO_DETAIL_SCREEN_BUNDLE_KEY_PODCAST_ID, podcast.id)
                navController.navigate(id, bundle)
            }
        }
        .padding(10.dp)) {
        //Thumbnail image
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(podcast.thumbnailUrl)
                .crossfade(true).build(),
            contentDescription = null,
            placeholder = painterResource(id = android.R.drawable.ic_menu_help),
            modifier = Modifier
                .size(100.dp)
                .padding(10.dp)
                .clip(RoundedCornerShape(20.dp))

        )
        //Text info
        Column {
            //Title
            Text(
                text = podcast.title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)
            )
            //Publisher
            Text(
                text = podcast.publisher,
                fontStyle = FontStyle.Italic,
                fontSize = 10.sp,
                color = PurpleGrey40
            )
            //Favorite text. Only show when the podcast is favorited by the user
            if (podcast.isFavor) {
                Text(
                    text = "Favourited", color = Color.Red, fontSize = 12.sp
                )
            }
        }
    }
}


package com.audiobooks.coding_challenge.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.audiobooks.coding_challenge.entites.PodcastEntity
import com.audiobooks.coding_challenge.ui.theme.PurpleGrey80
import com.audiobooks.coding_challenge.view_models.PodcastViewModel

@Composable
fun DetailScreen(
    viewModel: PodcastViewModel, podcastIndex: String, navController: NavHostController
) {
    //find the selected podcast data from the database and display
    viewModel.findPodcastById(podcastIndex)
    val data by viewModel.dataResults!!.observeAsState(listOf())
    if (!data.isEmpty()) {
        showDetail(data = data, viewModel, navController)
    }
}

@Composable
fun showDetail(
    data: List<PodcastEntity>, viewModel: PodcastViewModel, navController: NavHostController
) {
    Column {
        //Back button at the top left
        Button(colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent, contentColor = Color.Black
        ), onClick = { navController.navigateUp() }) {
            Text(text = "< Back")
        }
        //Detail column
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val podcast = data.get(0)
            //Title
            Text(
                text = podcast.title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 0.dp)
            )
            //Publisher
            Text(
                text = podcast.publisher,
                fontSize = 14.sp,
                color = PurpleGrey80,
                modifier = Modifier.padding(10.dp)
            )
            //Podcast thumbnail image
            AsyncImage(
                model = podcast.thumbnailUrl,
                placeholder = painterResource(id = android.R.drawable.ic_menu_help),
                contentDescription = null,
                modifier = Modifier
                    .weight(1f, fill = false)
                    .fillMaxWidth()
                    .padding(60.dp, 10.dp)
                    .clip(RoundedCornerShape(30.dp))
            )
            //Favorite button
            var favoriteText by remember { mutableStateOf(if (podcast.isFavor) "Favourited" else "Favourite") }
            Button(colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red, contentColor = Color.White
            ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp),
                onClick = {
                    podcast.isFavor = !podcast.isFavor
                    favoriteText = if (podcast.isFavor) "Favourited" else "Favourite"
                    viewModel.updatePodcastByObject(podcast)
                }) {
                Text(text = favoriteText)
            }
            //Description
            Text(
                text = podcast.description,
                color = Color.Gray,
                fontSize = 12.sp,
                lineHeight = 15.sp,
                modifier = Modifier.padding(20.dp, 10.dp)
            )
        }
    }
}
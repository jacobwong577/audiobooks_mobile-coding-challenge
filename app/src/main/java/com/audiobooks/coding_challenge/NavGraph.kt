package com.audiobooks.coding_challenge

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.audiobooks.coding_challenge.Constants.NAV_TO_DETAIL_SCREEN_BUNDLE_KEY_PODCAST_ID
import com.audiobooks.coding_challenge.screens.DetailScreen
import com.audiobooks.coding_challenge.screens.ListPodcastScreen
import com.audiobooks.coding_challenge.screens.Screens

@Composable
fun SetupNav(navController: NavHostController, context: Context) {
    //Setup screen navigation and use ListPodcastScreen as the starting screen
    NavHost(navController = navController, startDestination = Screens.ListScreen.route) {
        composable(route = Screens.ListScreen.route) {
            ListPodcastScreen(viewModel(), navController)
        }
        composable(route = Screens.DetailScreen.route) {
            val bundle = it.arguments
            val podcastId = bundle?.getString(NAV_TO_DETAIL_SCREEN_BUNDLE_KEY_PODCAST_ID)
            if (podcastId != null) {
                DetailScreen(viewModel(), podcastId, navController)
            }
        }
    }
}
package com.audiobooks.coding_challenge.screens

import com.audiobooks.coding_challenge.Constants

sealed class Screens(val route: String) {
    object ListScreen : Screens(Constants.NAV_LIST_SCREEN_NAME)
    object DetailScreen : Screens(Constants.NAV_DETAIL_SCREEN_NAME)
}
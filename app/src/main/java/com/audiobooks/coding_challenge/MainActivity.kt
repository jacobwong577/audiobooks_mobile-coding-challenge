package com.audiobooks.coding_challenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.audiobooks.coding_challenge.daos.AppDBs
import com.audiobooks.coding_challenge.daos.PodcastDAO
import com.audiobooks.coding_challenge.entites.PodcastEntity
import com.audiobooks.coding_challenge.ui.theme.AudiobooksChallengeTheme
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class MainActivity : ComponentActivity() {

    lateinit var navController: NavHostController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDBs.getInstance(this)
        val podcastDAO = db.podcastDao()
        setContent {
            AudiobooksChallengeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                }
                //setup screen navigation.
                navController = rememberNavController()
                SetupNav(navController = navController)
            }
        }
        Thread { callListenNotes(podcastDAO) }.start()
    }
}

fun callListenNotes(podcastDAO: PodcastDAO): String {
    //The api library gave me this error "com.listennotes.podcast_api.exception.NotFoundException: Endpoint not exist, or podcast / episode not exist" so I made the API using OkHttp.
    /*
     try {
         val objClient = Client("")
         val parameters = HashMap<String, String>()
         parameters.put("genre_id", "93")
         parameters.put("page", "2")
         parameters.put("region", "us")
         parameters.put("sort", "listen_score")
         parameters.put("safe_mode", "0")
         val response = objClient.fetchBestPodcasts(parameters)
     }  catch (e: ListenApiException) {
         println(e)
     }*/

    val client = OkHttpClient()

    val request = Request.Builder()
        .url("https://listen-api-test.listennotes.com/api/v2/best_podcasts?genre_id=93&page=2&region=us&sort=listen_score&safe_mode=0")
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {}
        override fun onResponse(call: Call, response: Response) {
            val outputString = response.body?.string()
            if (outputString != null) {
                val responseInJSON = JSONObject(outputString)
                val podcastsJSON = responseInJSON.getJSONArray("podcasts")
                for (i in 0 until podcastsJSON.length()) {
                    val eachJSON = podcastsJSON.getJSONObject(i)
                    val podcastObject = PodcastEntity(
                        eachJSON.getString("id"),
                        eachJSON.getString("title"),
                        eachJSON.getString("publisher"),
                        eachJSON.getString("thumbnail"),
                        eachJSON.getString("description")
                    )
                    podcastDAO.insertPodcast(podcastObject)
                }
            }
        }
    })
    return ""
}
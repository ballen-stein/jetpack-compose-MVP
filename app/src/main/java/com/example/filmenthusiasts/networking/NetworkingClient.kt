package com.example.filmenthusiasts.networking

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.lang.Exception

class NetworkingClient {
    private val client = OkHttpClient().newBuilder().build()

    private var url = "https://interview.zocdoc.com/api/1/FEE/"

    fun buildUrl(endpoint: String, auth: String, extraRequirements: List<String>? = listOf()): Request.Builder {
        this.url += "${endpoint}authToken=$auth"
        if (endpoint == MOVIES_BY_RANK_ENDPOINT && !extraRequirements.isNullOrEmpty()) {
            this.url += extraRequirements.let {
                "&startRankIndex=${it[0]}&numMovies=${it[1]}"
            }
        } else if (endpoint == MOVIE_DETAILS_ENDPOINT && !extraRequirements.isNullOrEmpty()) {
            extraRequirements.let {
                for (id in it) {
                    this.url += "&movieIds=$id"
                }
            }
        }

        return Request.Builder().url(url)
    }

    fun getMovieData(): Response {
        val request = Request.Builder()
            .url(url)
            .build()

        return client.newCall(request).execute()
    }

    fun createMoviePoster(movieTitle: String): Response {
        val posterUrl = "https://via.placeholder.com/170x225.png?text=$movieTitle"
        var request = Request.Builder()
            .url(posterUrl)
            .build()

        return try {
            client.newCall(request).execute()
        } catch (e: Exception) {
            // Catching exception where the website's certificate is down or expired and swapping to http
            request = Request.Builder()
                .url(posterUrl.replace("https", "http"))
                .build()
            client.newCall(request).execute()
        }
    }

    companion object {
        const val ALL_MOVIES_ENDPOINT = "AllMovies?"
        const val MOVIES_BY_RANK_ENDPOINT = "MoviesByRank?"
        const val MOVIE_DETAILS_ENDPOINT = "MovieDetails?"
    }
}
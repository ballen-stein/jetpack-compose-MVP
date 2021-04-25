package com.example.filmenthusiasts.model

import android.graphics.Bitmap
import com.squareup.moshi.Json
import java.io.Serializable

data class MovieData(
    @Json(name = "Id") val id: String = "",
    @Json(name = "Rank") val rank: String = "",
    @Json(name = "Name") val name: String = "",
    @Json(name = "Duration") val duration: String = "",
    @Json(name = "Description") val description: String = "",
    @Json(name = "Director") val director: String = "",
    @Json(name = "Genres") val genres: List<String>? = null,
    @Json(name = "Actors") val actors: List<String>? = null
) {
    @Transient private var poster: Bitmap? = null

    fun setBitmapPoster(poster: Bitmap) {
        this.poster = poster
    }

    fun getBitmapPoster(): Bitmap? {
        return poster
    }
}
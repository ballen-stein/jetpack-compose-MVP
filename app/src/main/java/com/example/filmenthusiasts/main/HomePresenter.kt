package com.example.filmenthusiasts.main

import android.content.Context
import com.example.filmenthusiasts.R
import com.example.filmenthusiasts.model.MovieData
import com.example.filmenthusiasts.networking.NetworkingClient
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.*
import okhttp3.Response
import java.lang.reflect.ParameterizedType
import kotlin.coroutines.CoroutineContext
import android.graphics.BitmapFactory

import android.graphics.Bitmap
import android.net.ConnectivityManager
import com.example.filmenthusiasts.errorcodes.ErrorCodes
import java.io.InputStream
import java.lang.Exception


class HomePresenter (view: HomeContract.View,
                     private val mContext: Context,
                     private val networkingClient: NetworkingClient,
                     override val coroutineContext: CoroutineContext
) : HomeContract.Presenter, CoroutineScope {

    private var view: HomeContract.View? = view
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private lateinit var adapter: JsonAdapter<List<MovieData>>

    private var endpointRequirements: List<String>? = emptyList()
    lateinit var urlEndpoint: String

    private var allData: List<MovieData> = arrayListOf()

    private var finishedRequests: Boolean = false

    override fun onViewCreated(
        urlEndpoint: String,
        filmRangeToView: List<String>?
    ) {
        this.urlEndpoint = urlEndpoint
        this.endpointRequirements = filmRangeToView

        val type: ParameterizedType = Types.newParameterizedType(List::class.java, MovieData::class.java)
        adapter = moshi.adapter(type)
    }

    override fun makeApiRequest() {
        if (connectionValid()) {
            startNetworkRequest()
        } else {
            view?.error(ErrorCodes.ERROR_CODE_NO_INTERNET)
        }
    }

    private fun connectionValid(): Boolean {
        val cManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cManager.activeNetwork != null
    }

    private fun startNetworkRequest() {
        try {
            networkingClient
                .buildUrl(
                    endpoint = urlEndpoint,
                    auth = mContext.getString(R.string.token),
                    extraRequirements = endpointRequirements
                )

            launch {
                val response = networkingClient.getMovieData()
                if (response.isSuccessful) {
                    parseApiResponse(response)
                } else {
                    view?.error(response.code)
                }
            }
        } catch (e: Exception) {
            view?.error(ErrorCodes.ERROR_CODE_NO_INTERNET)
            e.printStackTrace()
        }
    }

    private fun parseApiResponse(response: Response) {
        when (response.code) {
            200 -> {
                response.body?.let {
                    adapter.fromJson(it.string())?.let { movieList ->
                        allData = movieList
                    }
                    it.close()
                }

                createMoviePosters()
            }
            404 -> {
                view?.error(response.code)
            }
        }

    }

    private fun createMoviePosters() {
        launch {
            for (index in allData.indices) {
                val response = networkingClient.createMoviePoster(allData[index].name)
                parseMoviePosters(response, allData[index], index)
            }
        }
    }

    private fun parseMoviePosters(response: Response, movie: MovieData, index: Int) {
        lateinit var bitmap: Bitmap
        if (index == allData.indices.last) {
            finishedRequests = true
        }

        response.body?.let {
            val inputStream: InputStream = it.byteStream()
            bitmap = BitmapFactory.decodeStream(inputStream)
            it.close()
        }
        movie.setBitmapPoster(bitmap)

        if (finishedRequests) {
            view?.updateMovieList(allData = allData)
        }
    }

    override fun restoreDataState(): List<MovieData> {
        return allData
    }

    override fun onDestroy() {
        this.view = null
    }
}
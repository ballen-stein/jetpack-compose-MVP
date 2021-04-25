package com.example.filmenthusiasts.moviedetails

import android.content.Context
import com.example.filmenthusiasts.R
import com.example.filmenthusiasts.model.MovieData
import com.example.filmenthusiasts.networking.NetworkingClient
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.Response
import java.lang.reflect.ParameterizedType
import kotlin.coroutines.CoroutineContext

class MovieDetailsPresenter (view: MovieDetailsContract.View,
                             private val mContext: Context,
                             private val networkingClient: NetworkingClient,
                             override val coroutineContext: CoroutineContext
) : MovieDetailsContract.Presenter, CoroutineScope {

    private var view: MovieDetailsContract.View? = view
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private lateinit var adapter: JsonAdapter<List<MovieData>>
    private var allData: List<MovieData> = arrayListOf()

    private val urlEndpoint = NetworkingClient.MOVIE_DETAILS_ENDPOINT
    private var endpointRequirements: List<String>? = listOf()

    override fun onViewCreated(endpointRequirements: List<String>?) {
        this.endpointRequirements = endpointRequirements

        val type: ParameterizedType = Types.newParameterizedType(List::class.java, MovieData::class.java)
        adapter = moshi.adapter(type)

        networkingClient.buildUrl(
            endpoint = urlEndpoint,
            auth = mContext.getString(R.string.token),
            extraRequirements = endpointRequirements
        )

        launch {
            val response = networkingClient.getMovieData()
            parseApiResponse(response)
        }
    }

    private fun parseApiResponse(response: Response) {
        response.body?.let {
            adapter.fromJson(it.string())?.let { movieList ->
                allData = movieList
            }
            it.close()
        }
        view?.updateDialogView(allData[0])
    }

    override fun onDestroy() {
        this.view = null
    }

}

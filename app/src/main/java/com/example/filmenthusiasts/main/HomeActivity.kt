package com.example.filmenthusiasts.main

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.filmenthusiasts.R
import com.example.filmenthusiasts.errorcodes.ErrorCodes
import com.example.filmenthusiasts.model.MovieData
import com.example.filmenthusiasts.moviedetails.MovieDetailsBottomDialog
import com.example.filmenthusiasts.networking.NetworkingClient
import com.example.filmenthusiasts.ui.MovieScreenDisplay
import com.example.filmenthusiasts.webview.MovieWebView
import kotlinx.coroutines.Job

class HomeActivity : AppCompatActivity(), HomeContract.View {

    private lateinit var presenter: HomeContract.Presenter

    private var moviePoster: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState?.containsKey(ACCESS_SAVED_STATE) == true) {
            val savedState = savedInstanceState.get(ACCESS_SAVED_STATE)
            if (savedState is HomeContract.Presenter) {
                restoreState(savedState)
            } else {
                freshSetup()
            }
        } else {
            freshSetup()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        //outState.putSerializable(ACCESS_SAVED_STATE, presenter)
    }

    private fun freshSetup() {
        setPresenter(
            HomePresenter(
                view = this,
                mContext = this,
                networkingClient = NetworkingClient(),
                coroutineContext = Job()
            )
        )

        presenter.onViewCreated(
            urlEndpoint = NetworkingClient.MOVIES_BY_RANK_ENDPOINT,
            filmRangeToView = listOf("1", "10")
        )

        setContent {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = Color(getColor(R.color.teal_700))
                )
            }
        }

        presenter.makeApiRequest()
    }


    lateinit var allData: List<MovieData>

    private fun restoreState(savedState: HomeContract.Presenter) {
        presenter = savedState
        allData = presenter.restoreDataState()
        setUi()
    }

    override fun updateMovieList(allData: List<MovieData>) {
        this.allData = allData
        setUi()
    }

    private fun setUi() {
        runOnUiThread {
            setContent {
                MovieScreenDisplay.MovieList(movieList = allData, context = this, true)
            }
        }
    }

    fun showMessage(movieData: MovieData) {
        moviePoster = movieData.getBitmapPoster()

        MovieDetailsBottomDialog.newInstance(Bundle().apply {
            putString(MovieDetailsBottomDialog.ARG_MOVIE_ID, movieData.id)
        }).show(supportFragmentManager, movieData.id)
    }

    fun launchBuyingWebView() {
        if (supportFragmentManager.fragments.size > 0) {
            supportFragmentManager.fragments.forEach { fragment ->
                if (fragment is MovieDetailsBottomDialog) {
                    fragment.dismiss()
                }
            }
        }

        startActivity(Intent(this, MovieWebView::class.java))
    }

    fun getMoviePoster(): Bitmap? {
        return moviePoster
    }

    override fun error(code: Int) {
        val text = if (code == ErrorCodes.ERROR_CODE_NOT_FOUND) getString(R.string.error_not_found) else getString(R.string.error_no_internet)
        runOnUiThread {
            setContent{
                Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                   Text(
                       text = text,
                       textAlign = TextAlign.Center,
                       fontSize = 24.sp,
                       color = Color.White
                   )
                }
            }
        }
    }

    override fun setPresenter(presenter: HomeContract.Presenter) {
        this.presenter = presenter
    }

    companion object {
        const val ACCESS_SAVED_STATE = "saved"
    }
}
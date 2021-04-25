package com.example.filmenthusiasts.main

import com.example.filmenthusiasts.model.MovieData
import com.example.filmenthusiasts.mvp.MovieEnthusiastPresenter
import com.example.filmenthusiasts.mvp.MovieEnthusiastView
import java.io.Serializable

interface HomeContract {
    interface Presenter : MovieEnthusiastPresenter, Serializable {
        fun onViewCreated(urlEndpoint: String, filmRangeToView: List<String>?)
        fun makeApiRequest()
        fun restoreDataState(): List<MovieData>
    }

    interface View : MovieEnthusiastView<Presenter> {
        fun updateMovieList(allData: List<MovieData>)
        fun error(code: Int)
    }
}
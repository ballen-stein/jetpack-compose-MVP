package com.example.filmenthusiasts.moviedetails

import com.example.filmenthusiasts.model.MovieData
import com.example.filmenthusiasts.mvp.MovieEnthusiastPresenter
import com.example.filmenthusiasts.mvp.MovieEnthusiastView

class MovieDetailsContract {
    interface Presenter : MovieEnthusiastPresenter {
        fun onViewCreated(endpointRequirements: List<String>?)
    }

    interface View : MovieEnthusiastView<Presenter> {
        fun updateDialogView(movieData: MovieData)
        fun error(throwable: Throwable)
    }
}
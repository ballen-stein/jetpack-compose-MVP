package com.example.filmenthusiasts.moviedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.filmenthusiasts.databinding.EmptyFragmentBinding
import com.example.filmenthusiasts.model.MovieData
import com.example.filmenthusiasts.networking.NetworkingClient
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Job
import com.example.filmenthusiasts.main.HomeActivity
import com.example.filmenthusiasts.ui.MovieScreenDisplay

class MovieDetailsBottomDialog : BottomSheetDialogFragment(), MovieDetailsContract.View {

    lateinit var binding: EmptyFragmentBinding

    private lateinit var presenter: MovieDetailsContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EmptyFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setPresenter(
            MovieDetailsPresenter(
                view = this,
                mContext = requireContext(),
                networkingClient = NetworkingClient(),
                coroutineContext = Job()
            )
        )
        val endpointRequirements = if (requireArguments().containsKey(ARG_MOVIE_ID)) {
            listOf(requireArguments().getString(ARG_MOVIE_ID)!!)
        } else {
            listOf()
        }

        presenter.onViewCreated(endpointRequirements = endpointRequirements)
    }

    override fun updateDialogView(movieData: MovieData) {
        val poster = (requireActivity() as HomeActivity).getMoviePoster()
        poster?.let { movieData.setBitmapPoster(poster = it) }
        binding.composeView.setContent {
            MovieScreenDisplay.MovieFragmentData(movieData = movieData, requireContext())
        }
    }

    override fun error(throwable: Throwable) { }

    override fun setPresenter(presenter: MovieDetailsContract.Presenter) {
        this.presenter = presenter
    }

    companion object {
        const val ARG_MOVIE_ID = "MOVIE_ID"

        fun newInstance(b: Bundle?): MovieDetailsBottomDialog {
            val fragment = MovieDetailsBottomDialog()
            fragment.arguments = Bundle(b)
            return fragment
        }
    }
}
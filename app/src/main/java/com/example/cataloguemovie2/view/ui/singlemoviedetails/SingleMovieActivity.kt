package com.example.cataloguemovie2.view.ui.singlemoviedetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.cataloguemovie2.R
import com.example.cataloguemovie2.data.api.POSTER_BASE_URL
import com.example.cataloguemovie2.data.api.TheMovieDBClient
import com.example.cataloguemovie2.data.api.TheMovieDBInterface
import com.example.cataloguemovie2.data.model.MovieDetails
import com.example.cataloguemovie2.data.repository.MovieDetailsRepository
import com.example.cataloguemovie2.data.repository.NetworkState
import com.example.cataloguemovie2.viewmodel.SingleMovieViewModel
import kotlinx.android.synthetic.main.activity_single_movie.*
import java.text.NumberFormat
import java.util.*

class SingleMovieActivity : AppCompatActivity() {

    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieRepository: MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)

        val movieId: Int = intent.getIntExtra("id", 1)

        val apiService : TheMovieDBInterface = TheMovieDBClient.getClient().also {
            movieRepository = MovieDetailsRepository(it)
        }

        viewModel = getViewModel(movieId).also { singleMovieViewModel ->
            singleMovieViewModel.movieDetails.observe(this, Observer {
                bindUi(it)
            })
            singleMovieViewModel.networkState.observe(this, Observer {
                //show progress bar when loading and show error text when error occured
                progressCircular.visibility = if(it == NetworkState.LOADING) View.VISIBLE else View.GONE
                textViewError.visibility = if(it == NetworkState.ERROR) View.VISIBLE else View.GONE
            })
        }
    }

    fun bindUi(it: MovieDetails) {
        textViewMovieTitle.text = it.title
        textViewMovieTagline.text = it.tagline
        textViewMovieReleaseDate.text = it.releaseDate
        textViewMovieRating.text = it.rating.toString()
        textViewMovieRuntime.text = it.runtime.toString() + " minutes"
        textViewMovieOverview.text = it.overview

        NumberFormat.getCurrencyInstance(Locale.US).run {
            textViewMovieBudget.text = this.format(it.budget)
            textViewMovieRevenue.text = this.format(it.revenue)
        }

        val moviePosterURL = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(ivMoviePoster)
    }

    private fun getViewModel(movieId: Int): SingleMovieViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleMovieViewModel(
                    movieRepository,
                    movieId
                ) as T
            }
        })[SingleMovieViewModel::class.java] //class reference
    }
}

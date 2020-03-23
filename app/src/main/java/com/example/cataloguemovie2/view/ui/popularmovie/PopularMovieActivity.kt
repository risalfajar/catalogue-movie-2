/*
 * Copyright (c) 2020.
 * By Risal Fajar Amiyardi
 * risalfajar@gmail.com | github.com/risalfajar
 */

package com.example.cataloguemovie2.view.ui.popularmovie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cataloguemovie2.R
import com.example.cataloguemovie2.data.api.TheMovieDBClient
import com.example.cataloguemovie2.data.api.TheMovieDBInterface
import com.example.cataloguemovie2.data.repository.NetworkState
import com.example.cataloguemovie2.view.ui.singlemoviedetails.SingleMovieActivity
import com.example.cataloguemovie2.viewmodel.PopularMovieViewModel
import kotlinx.android.synthetic.main.activity_main.*

class PopularMovieActivity : AppCompatActivity() {

    private lateinit var viewModel: PopularMovieViewModel
    lateinit var movieRepository: MoviePagedListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService: TheMovieDBInterface = TheMovieDBClient.getClient()
        movieRepository = MoviePagedListRepository(apiService)
        viewModel = getViewModel()

        val movieAdapter = PopularMoviePagedListAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 3).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
                override fun getSpanSize(position: Int): Int {
                    val viewType = movieAdapter.getItemViewType(position)
                    if(viewType == movieAdapter.MOVIE_VIEW_TYPE) return 1
                    else return 3
                }
            }
        }

        rvMovieList.apply {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            adapter = movieAdapter
        }

        viewModel.also { vM ->
            vM.moviePagedList.observe(this, Observer {
                movieAdapter.submitList(it)
            })

            vM.networkState.observe(this, Observer {
                progressCircular.visibility = if(vM.isListEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
                textViewError.visibility = if(vM.isListEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

                if(!vM.isListEmpty()) movieAdapter.setNetworkState(it)
            })
        }
    }

    private fun getViewModel(): PopularMovieViewModel{
        return ViewModelProvider(this, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PopularMovieViewModel(movieRepository) as T
            }
        })[PopularMovieViewModel::class.java]
    }
}

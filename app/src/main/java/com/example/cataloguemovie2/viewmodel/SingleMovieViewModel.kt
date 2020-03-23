/*
 * Copyright (c) 2020.
 * By Risal Fajar Amiyardi
 * risalfajar@gmail.com | github.com/risalfajar
 */

package com.example.cataloguemovie2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.cataloguemovie2.data.model.MovieDetails
import com.example.cataloguemovie2.data.repository.MovieDetailsRepository
import com.example.cataloguemovie2.data.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class SingleMovieViewModel(private val movieRepository: MovieDetailsRepository, movieId: Int) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    //we use lazy so that we get the data when we need it,
    //not when ViewModel is initialized (for better performance)
    val movieDetails : LiveData<MovieDetails> by lazy {
        movieRepository.fetchSingleMovieDetails(compositeDisposable, movieId)
    }

    val networkState : LiveData<NetworkState> by lazy {
        movieRepository.getMovieDetailsNetworkState()
    }

    //called when activity/fragment gets destroyed
    override fun onCleared() {
        super.onCleared()
        //dispose the composite to avoid memory leaks
        compositeDisposable.dispose()
    }
}
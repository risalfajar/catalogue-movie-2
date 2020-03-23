/*
 * Copyright (c) 2020.
 * By Risal Fajar Amiyardi
 * risalfajar@gmail.com | github.com/risalfajar
 */

package com.example.cataloguemovie2.data.repository

import androidx.lifecycle.LiveData
import com.example.cataloguemovie2.data.api.TheMovieDBInterface
import com.example.cataloguemovie2.data.model.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(private val apiService: TheMovieDBInterface) {
    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    //fetch MovieDetails from network and return as LiveData
    fun fetchSingleMovieDetails(compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MovieDetails>{
        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource.run {
            this.fetchMovieDetails(movieId)
            return this.downloadedMovieDetailsResponse
        }
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState>{
        return movieDetailsNetworkDataSource.networkState
    }
}
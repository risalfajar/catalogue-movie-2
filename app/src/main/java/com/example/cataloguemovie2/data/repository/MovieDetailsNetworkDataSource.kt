/*
 * Copyright (c) 2020.
 * By Risal Fajar Amiyardi
 * risalfajar@gmail.com | github.com/risalfajar
 */

package com.example.cataloguemovie2.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cataloguemovie2.data.api.TheMovieDBInterface
import com.example.cataloguemovie2.data.model.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

//here we'll call API using RxJava
//our API will return MovieDetails, then we assign it to a LiveData
class MovieDetailsNetworkDataSource(
    private val apiService: TheMovieDBInterface,
    //CompositeDisposable is a RxJava component that can be used to dispose API calls
    private val compositeDisposable: CompositeDisposable
) {
    //underscore means private
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState //with this get, no need to implement get function to get networkState

    private val _downloadedMovieDetailsResponse = MutableLiveData<MovieDetails>()
    val downloadedMovieDetailsResponse: LiveData<MovieDetails>
        get() = _downloadedMovieDetailsResponse

    fun fetchMovieDetails(movieId: Int){
        _networkState.postValue(NetworkState.LOADING)
        try {
            //using RxJava thread to make network calls
            //we want this thread to be disposable, so add it to compositeDisposable
            compositeDisposable.add(
                //this function returns a Single observable, so we can subscribe on that
                apiService.getMovieDetails(movieId)
                    //here we are subscribing on Schedulers.io thread pool to observe this network call
                    .subscribeOn(Schedulers.io()) //Schedulers.io is a thread pool
                    .subscribe({
                        //if the network call is success, we get MovieDetails
                        _downloadedMovieDetailsResponse.postValue(it)
                        _networkState.postValue(NetworkState.LOADED)
                    },{
                        _networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDetailsNetworkData", it.message ?: "Unknown error")
                    })

            )
        }catch(e: Exception){
            Log.e("MovieDetailsNetworkData", e.message ?: "Unknown error")
        }
    }

}